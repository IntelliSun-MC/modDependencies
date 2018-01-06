package net.intellisun.moddependencies

import groovy.transform.ToString

@ToString
class DevDependency {
	String name
	String group
	String version
	
	private DevDependency() {
	}
	
	private DevDependency(String group, String name, String version) {
		this.name = name
		this.group = group
		this.version = version
	}
	
	static DevDependency fromClosure(final Closure<?> configureClosure) {
		def dependency = new DevDependency()
		dependency.with(configureClosure)
		
		return dependency
	}
	
	static DevDependency create(String group, String name, String version) {
		return new DevDependency(group, name, version)
	}
	
	static DevDependency create(String dependencyString) {
		def dependencyParts = dependencyString.split(':')
		return new DevDependency(
			dependencyParts[0],
			dependencyParts[1],
			dependencyParts[2]
		)
	}
	
	static DevDependency create(Map args) {
		return new DevDependency(
			args.group,
			args.name,
			args.version
		)
	}
	
	def toMap() {
		return [ group: this.group, name: this.name, version: this.version ]
	}
}
