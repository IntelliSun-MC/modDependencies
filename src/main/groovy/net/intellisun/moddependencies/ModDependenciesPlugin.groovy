package net.intellisun.moddependencies

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.NamedDomainObjectContainer

import groovy.transform.ToString

class ModDependenciesPlugin implements Plugin<Project> {
	private static final String EXTENSION_NAME = 'modDependencies'
	
	Project project
	NamedDomainObjectContainer<ModDependency> extension

	@Override
	void apply(Project project) {
		this.project = project
		this.extension = this.project.container(ModDependency)
		
		this.loadExtension()
		
		this.project.afterEvaluate {
			this.applyDependencies()
		}
	}
	
	private void loadExtension() {
		this.project.extensions.add(EXTENSION_NAME, this.extension)
		
		this.extension.all {
			plugin = this
			configurations = this.project.configurations
		}
	}
	
	private void applyDependencies() {
		this.project.configurations
	}
	
	private void updateDependencyString() {
		def runtimeDeps = this.extension
			.findAll { dep -> dep.runtimeDependency != null && dep.shouldLoadForBuild }
			.collect { dep -> dep.runtimeDependency.toDependencyString() }
			
		project.ext.dependencyString = String.join(',', runtimeDeps)
	}
}
