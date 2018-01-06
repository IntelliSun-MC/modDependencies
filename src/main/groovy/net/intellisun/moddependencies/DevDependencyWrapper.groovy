package net.intellisun.moddependencies

import org.gradle.api.artifacts.dsl.DependencyHandler
import groovy.transform.ToString

@ToString
class DevDependencyWrapper {
	String configuration
	DevDependency dependency
	
	static DevDependencyWrapper create(String configuration, args) {
		def wrapper = new DevDependencyWrapper(configuration)
		if (args.size() == 1)
		{
			def arg = args.getAt(0)
			if (arg instanceof Closure)
				return wrapper.init((Closure)arg)
			
			if (arg instanceof Map)
				return wrapper.init((Map)arg)
			
			return wrapper.init(arg.toString())
		}
		
		if (args.size() == 3)
			return wrapper.init(
				args.getAt(0).toString(),
				args.getAt(1).toString(),
				args.getAt(2).toString()
			)
			
		return null
	}
	
	private DevDependencyWrapper(String configuration) {
		this.configuration = configuration
	}
	
	def addToProject(DependencyHandler handler) {
		handler.add(this.configuration, this.dependency.toMap())
	}
	
	def init(final Closure<?> configureClosure) {
		this.setDevDependency(DevDependency.fromClosure(configureClosure))
		return this;
	}
	
	def init(String group, String name, String version) {
		this.setDevDependency(DevDependency.create(group, name, version))
		return this;
	}
	
	def init(String dependencyString) {
		this.setDevDependency(DevDependency.create(dependencyString))
		return this;
	}
	
	def init(Map args) {
		this.setDevDependency(DevDependency.create(args))
		return this;
	}
	
	private void setDevDependency(DevDependency dependency) {
		this.dependency = dependency
	}
}

