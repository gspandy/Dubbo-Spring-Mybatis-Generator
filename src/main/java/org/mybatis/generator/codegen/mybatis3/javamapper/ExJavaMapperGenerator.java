/*
 *  Copyright 2009 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.mybatis.generator.codegen.mybatis3.javamapper;

import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.extension.*;

/**
 * @author chunxiao
 * 方法返回值都用包装类型
 */
public class ExJavaMapperGenerator extends JavaMapperGenerator {

	/**
     *
     */
	public ExJavaMapperGenerator() {
		super(true);
	}


	@Override
	protected void addDeleteByPrimaryKeyMethod(Interface interfaze) {
		if (introspectedTable.getRules().generateDeleteByPrimaryKey()) {
			AbstractJavaMapperMethodGenerator methodGenerator = new DeleteByPrimaryKeyMethodGenerator();
			initializeAndExecuteGenerator(methodGenerator, interfaze);
		}
	}

	@Override
	protected void addInsertMethod(Interface interfaze) {
		if (introspectedTable.getRules().generateInsert()) {
			AbstractJavaMapperMethodGenerator methodGenerator = new InsertMethodGenerator();
			initializeAndExecuteGenerator(methodGenerator, interfaze);
		}
	}

	@Override
	protected void addInsertSelectiveMethod(Interface interfaze) {
		if (introspectedTable.getRules().generateInsertSelective()) {
			AbstractJavaMapperMethodGenerator methodGenerator = new InsertSelectiveMethodGenerator();
			initializeAndExecuteGenerator(methodGenerator, interfaze);
		}
	}

	@Override
	protected void addUpdateByPrimaryKeySelectiveMethod(Interface interfaze) {
		if (introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
			AbstractJavaMapperMethodGenerator methodGenerator = new UpdateByPrimaryKeySelectiveMethodGenerator();
			initializeAndExecuteGenerator(methodGenerator, interfaze);
		}
	}

	@Override
	protected void addUpdateByPrimaryKeyWithBLOBsMethod(Interface interfaze) {
		if (introspectedTable.getRules().generateUpdateByPrimaryKeyWithBLOBs()) {
			AbstractJavaMapperMethodGenerator methodGenerator = new UpdateByPrimaryKeyWithBLOBsMethodGenerator();
			initializeAndExecuteGenerator(methodGenerator, interfaze);
		}
	}

	@Override
	protected void addUpdateByPrimaryKeyWithoutBLOBsMethod(Interface interfaze) {
		if (introspectedTable.getRules().generateUpdateByPrimaryKeyWithoutBLOBs()) {
			AbstractJavaMapperMethodGenerator methodGenerator = new UpdateByPrimaryKeyWithoutBLOBsMethodGenerator();
			initializeAndExecuteGenerator(methodGenerator, interfaze);
		}
	}

	//todo 生成list方法
	@Override
	protected void addListQueryMethod(Interface interfaze) {
		AbstractJavaMapperMethodGenerator methodGenerator = new ListMethodGenerator();
		initializeAndExecuteGenerator(methodGenerator, interfaze);
	}
}
