package org.bimserver.scenejs;

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

import org.bimserver.plugins.PluginException;
import org.bimserver.plugins.PluginManager;
import org.bimserver.plugins.serializers.EmfSerializer;
import org.bimserver.plugins.serializers.SerializerPlugin;

public class SceneJSSerializerPlugin implements SerializerPlugin {

	private boolean initialized = false;

	@Override
	public String getDescription() {
		return "SceneJSSerializer";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public void init(PluginManager pluginManager) throws PluginException {
		pluginManager.requireSchemaDefinition();
		pluginManager.requireIfcEngine();
		initialized = true;
	}

	@Override
	public EmfSerializer createSerializer() {
		return new SceneJSSerializer();
	}

	@Override
	public String getDefaultSerializerName() {
		return "SceneJS";
	}

	@Override
	public String getDefaultContentType() {
		return "application/javascript";
	}

	@Override
	public String getDefaultExtension() {
		return "js";
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}
}
