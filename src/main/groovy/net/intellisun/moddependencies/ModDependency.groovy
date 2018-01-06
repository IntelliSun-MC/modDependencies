package net.intellisun.moddependencies

import org.gradle.api.artifacts.ConfigurationContainer

class ModDependency {
	final Collection<ModDependency> dependencies = new ArrayList<>();
	
	String name // also mod id
	
	DevDependencyWrapper devDependency
	RuntimeDependency runtimeDependency
	
	boolean shouldLoadForBuild = true
	boolean shouldDownloadForBuild = true
	
	ModDependenciesPlugin plugin
	ConfigurationContainer configurations
	
	public ModDependency(final String name) {
		this.name = name // from container
		
		def mc = new ExpandoMetaClass(ModDependency, false, true)
        mc.initialize()
        this.metaClass = mc
	}
	
	def requireAfter(final Closure<?> configureClosure) {
		this.setRuntimeDependency(createDependency(true, 'after', null, configureClosure));
	}
	
	def requireAfter(String versionString = null, final Closure<?> configureClosure = null) {
		this.setRuntimeDependency(createDependency(true, 'after', versionString, configureClosure));
	}
	
	def requireBefore(final Closure<?> configureClosure) {
		this.setRuntimeDependency(createDependency(true, 'before', null, configureClosure));
	}
	
	def requireBefore(String versionString = null, final Closure<?> configureClosure = null) {
		this.setRuntimeDependency(createDependency(true, 'before', versionString, configureClosure));
	}
	
	def require(final Closure<?> configureClosure) {
		this.setRuntimeDependency(createDependency(true, null, null, configureClosure));
	}
	
	def require(String versionString = null, final Closure<?> configureClosure = null) {
		this.setRuntimeDependency(createDependency(true, null, versionString, configureClosure));
	}
	
	def loadAfter(final Closure<?> configureClosure) {
		this.setRuntimeDependency(createDependency(false, 'after', null, configureClosure));
	}
	
	def loadAfter(String versionString = null, final Closure<?> configureClosure = null) {
		this.setRuntimeDependency(createDependency(false, 'after', versionString, configureClosure));
	}
	
	def loadBefore(final Closure<?> configureClosure) {
		this.setRuntimeDependency(createDependency(false, 'before', null, configureClosure));
	}
	
	def loadBefore(String versionString = null, final Closure<?> configureClosure = null) {
		this.setRuntimeDependency(createDependency(false, 'before', versionString, configureClosure));
	}
	
	def load(final Closure<?> configureClosure) {
		this.setRuntimeDependency(createDependency(false, null, null, configureClosure));
	}
	
	def load(String versionString = null, final Closure<?> configureClosure = null) {
		this.setRuntimeDependency(createDependency(false, null, versionString, configureClosure));
	}
	
	def loadForBuild(boolean value) { 
		shouldLoadForBuild = value
	}
	
	def downloadForBuild(boolean value) {
		shouldDownloadForBuild = value
	}
	
	def methodMissing(String name, args) {
		def invokeResult = tryInvoke(name, args)
		if (invokeResult == null)
			throw new MissingMethodException(name, this.class, args)
			
		this.setDevDependency(invokeResult)
	}
	
	private DevDependencyWrapper tryInvoke(String name, args) {
		if (args.length == 0 || configurations.findByName(name) == null)
			return null
			
		return DevDependencyWrapper.create(name, args)
	}
	
	private RuntimeDependency createDependency(boolean required, String loadOrder, String version, 
			final Closure<?> configureClosure = null) {
		def dependency = new RuntimeDependency(this.name)
		dependency.required = required
		
		if (loadOrder != null)
			dependency.loadOrder = loadOrder
		
		if (version != null)
			dependency.version("[${version},)")
			
		if (configureClosure != null)
			dependency.with(configureClosure)
			
		return dependency
	}
	
	private void setRuntimeDependency(RuntimeDependency dependency) {
		this.runtimeDependency = dependency
		
		if (shouldLoadForBuild)
			this.plugin.updateDependencyString()
	}
	
	private void setDevDependency(DevDependencyWrapper dependency) {
		this.devDependency = dependency
		
		if (shouldDownloadForBuild)
			this.devDependency.addToProject(this.plugin.project.dependencies)
	}
}
