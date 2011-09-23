package org.bimserver.tests;

/******************************************************************************
 * Copyright (C) 2011  BIMserver.org
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *****************************************************************************/

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bimserver.LocalDevPluginLoader;
import org.bimserver.models.ifc2x3.IfcAreaMeasure;
import org.bimserver.models.ifc2x3.IfcBuildingStorey;
import org.bimserver.models.ifc2x3.IfcElement;
import org.bimserver.models.ifc2x3.IfcElementQuantity;
import org.bimserver.models.ifc2x3.IfcExtrudedAreaSolid;
import org.bimserver.models.ifc2x3.IfcFeatureElementSubtraction;
import org.bimserver.models.ifc2x3.IfcOpeningElement;
import org.bimserver.models.ifc2x3.IfcProduct;
import org.bimserver.models.ifc2x3.IfcProductDefinitionShape;
import org.bimserver.models.ifc2x3.IfcProductRepresentation;
import org.bimserver.models.ifc2x3.IfcProfileDef;
import org.bimserver.models.ifc2x3.IfcProperty;
import org.bimserver.models.ifc2x3.IfcPropertySet;
import org.bimserver.models.ifc2x3.IfcPropertySetDefinition;
import org.bimserver.models.ifc2x3.IfcPropertySingleValue;
import org.bimserver.models.ifc2x3.IfcRectangleProfileDef;
import org.bimserver.models.ifc2x3.IfcRelContainedInSpatialStructure;
import org.bimserver.models.ifc2x3.IfcRelDefines;
import org.bimserver.models.ifc2x3.IfcRelDefinesByProperties;
import org.bimserver.models.ifc2x3.IfcRelVoidsElement;
import org.bimserver.models.ifc2x3.IfcRepresentation;
import org.bimserver.models.ifc2x3.IfcRepresentationItem;
import org.bimserver.models.ifc2x3.IfcShapeRepresentation;
import org.bimserver.models.ifc2x3.IfcSlab;
import org.bimserver.plugins.PluginException;
import org.bimserver.plugins.PluginManager;
import org.bimserver.plugins.deserializers.DeserializeException;
import org.bimserver.plugins.deserializers.DeserializerPlugin;
import org.bimserver.plugins.deserializers.EmfDeserializer;
import org.bimserver.plugins.serializers.IfcModelInterface;
import org.bimserver.querycompiler.QueryInterface;

public class SlabOpening implements QueryInterface {

	public static void main(String[] args) {
		PluginManager pluginManager;
		try {
			pluginManager = LocalDevPluginLoader.createPluginManager();
			DeserializerPlugin deserializerPlugin = pluginManager.requireDeserializer("ifc");
			EmfDeserializer deserializer = deserializerPlugin.createDeserializer();
			deserializer.init(pluginManager.requireSchemaDefinition());
			IfcModelInterface model = deserializer.read(new File(TestFileConstants.DATA_FOLDER, "4351.ifc"), true);
			new SlabOpening().query(model, new PrintWriter(System.out));
		} catch (PluginException e) {
			e.printStackTrace();
		} catch (DeserializeException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void query(IfcModelInterface model, PrintWriter out) {

		/*
		 * Checking how many element quantity is present in the given model. To
		 * ensure the model has element quantity
		 */
		List<IfcElementQuantity> qty = model.getAll(IfcElementQuantity.class);
		out.println("Total Element quantity in the model are " + qty.size());

		List<IfcBuildingStorey> stories = model.getAll(IfcBuildingStorey.class);
		List<IfcFeatureElementSubtraction> Opening_subjectToArea = new ArrayList<IfcFeatureElementSubtraction>();

		if (!(stories.isEmpty())) {
			for (IfcBuildingStorey storey : stories) {
				for (IfcRelContainedInSpatialStructure rel : storey.getContainsElements()) {
					for (IfcProduct product : rel.getRelatedElements()) {
						if (product instanceof IfcSlab) {
							IfcSlab tempSlab = (IfcSlab) product;
							IfcElement ifcslabElement = (IfcElement) tempSlab;
							for (IfcRelVoidsElement relVoids : ifcslabElement.getHasOpenings()) {
								Opening_subjectToArea.add(relVoids.getRelatedOpeningElement());
							}
						}
					}
				}
			}
		} else {
			out.println("Building stories not available...");
		}

		if (!(Opening_subjectToArea.isEmpty())) {
			out.println("Total Opening Element in the model which are associated to slabs are " + Opening_subjectToArea.size());
			Iterator OE_it = Opening_subjectToArea.iterator();
			while (OE_it.hasNext()) {
				IfcOpeningElement openingElement = (IfcOpeningElement) OE_it.next();
				
				IfcProductRepresentation representation = openingElement.getRepresentation();
				IfcProductDefinitionShape ifcProductDefinitionShape = (IfcProductDefinitionShape)representation;
				for (IfcRepresentation ifcRepresentation : ifcProductDefinitionShape.getRepresentations()) {
					if (ifcRepresentation instanceof IfcShapeRepresentation) {
						IfcShapeRepresentation ifcShapeRepresentation = (IfcShapeRepresentation)ifcRepresentation;
						for (IfcRepresentationItem item : ifcShapeRepresentation.getItems()) {
							if (item instanceof IfcExtrudedAreaSolid) {
								IfcExtrudedAreaSolid extrudedAreaSolid = (IfcExtrudedAreaSolid)item;
								IfcProfileDef sweptArea = extrudedAreaSolid.getSweptArea();
								if (sweptArea instanceof IfcRectangleProfileDef) {
									IfcRectangleProfileDef rectangleProfileDef = (IfcRectangleProfileDef)sweptArea;
									float area = rectangleProfileDef.getXDim() * rectangleProfileDef.getYDim();
									System.out.println("Area calculated from geometry: " + area);
								}
							}
						}
					}
				}
				
				for (IfcRelDefines ifcRelDefines : openingElement.getIsDefinedBy()) {
					if (ifcRelDefines instanceof IfcRelDefinesByProperties) {
						IfcRelDefinesByProperties ifcRelDefinesByProperties = (IfcRelDefinesByProperties) ifcRelDefines;
						IfcPropertySetDefinition relatingPropertyDefinition = ifcRelDefinesByProperties.getRelatingPropertyDefinition();
						if (relatingPropertyDefinition instanceof IfcPropertySet) {
							IfcPropertySet ifcPropertySet = (IfcPropertySet)relatingPropertyDefinition;
							for (IfcProperty ifcProperty : ifcPropertySet.getHasProperties()) {
								if (ifcProperty instanceof IfcPropertySingleValue) {
									IfcPropertySingleValue ifcPropertySingleValue = (IfcPropertySingleValue)ifcProperty;
									if (ifcPropertySingleValue.getNominalValue() instanceof IfcAreaMeasure) {
										IfcAreaMeasure ifcAreaMeasure = (IfcAreaMeasure)ifcPropertySingleValue.getNominalValue();
										System.out.println("Area from semantic: " + ifcAreaMeasure.getWrappedValue());
									}
								}
							}
						}
					}
				}
			}
		} else {
			out.println("No match for Opening element incorporated to slab");
		}
		out.flush();
	}
}