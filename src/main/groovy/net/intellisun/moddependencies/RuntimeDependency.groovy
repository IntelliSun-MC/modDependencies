package net.intellisun.moddependencies

import groovy.transform.ToString

@ToString
class RuntimeDependency {
	static final Set<String> VALID_LOADORDER_VALUES = [ 'none', 'before', 'after' ]
	static final Set<String> VALID_SIDED_DEPENDENCY = [ 'both', 'client', 'server' ]
	
	String modId
	
	Set<String> versionStrings = []
	
	boolean required
	
	String loadOrder
	String sidedDependency
	
	RuntimeDependency(String modId) {
		this.modId = modId
	
		this.loadOrder = 'none'
		this.sidedDependency = 'both'
		this.required = false
	}
	
	def version(String version) {
		this.versionStrings.add(version)
	}
	
	def serverSide() {
		this.sidedDependency = 'server'
	}
	
	def clientSide() {
		this.sidedDependency = 'client'
	}
	
	def loadBefore() {
		this.loadOrder = 'before'
	}
	
	def loadAfter() {
		this.loadOrder = 'after'
	}
	
	def required() {
		this.required = true
	}
	
	def optional() {
		this.required = false
	}
	
	def toDependencyString() {
		def options = []
		if (this.required)
			options.add('required')
		
		if (this.sidedDependency != 'both')
			options.add(this.sidedDependency)
		
		if (this.loadOrder != 'none')
			options.add(this.loadOrder)
			
		def result = "${String.join('-', options)}:${this.modId}"
		
		if (this.versionStrings.size() != 0) {
			def versionDef = String.join(',', this.versionStrings)
			result += "@${versionDef}"
		}
		
		return result
	}
}