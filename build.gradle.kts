plugins {
	java
	alias(libs.plugins.fabric.loom)
	alias(libs.plugins.minotaur)
}

val modId: String by project
val modVersion: String by project

version = "$modVersion+${libs.versions.minecraft.get()}"
base.archivesName = modId

loom {
	splitEnvironmentSourceSets()
}

repositories {
	mavenCentral()

	maven {
		name = "Spirit Studios Releases"
		url = uri("https://maven.spiritstudios.dev/releases/")

		content {
			@Suppress("UnstableApiUsage")
			includeGroupAndSubgroups("dev.spiritstudios")
		}
	}
}

dependencies {
	minecraft(libs.minecraft)
	mappings(variantOf(libs.yarn) { classifier("v2") })
	modImplementation(libs.fabric.loader)

	modImplementation(libs.fabric.api)
}

tasks.processResources {
	val map = mapOf(
		"version" to modVersion
	)

	inputs.properties(map)

	filesMatching("fabric.mod.json") { expand(map) }
}

java {
	withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<JavaCompile> {
	options.encoding = "UTF-8"
	options.release = 21
}

tasks.jar {
	from("LICENSE") { rename { "${it}_${base.archivesName.get()}" } }
}

modrinth {
	token.set(System.getenv("MODRINTH_TOKEN"))
	projectId.set("umbra_express")
	versionNumber.set("$modVersion+${libs.versions.minecraft.get()}")
	uploadFile.set(tasks.remapJar)
	gameVersions.addAll(libs.versions.minecraft.get())
	loaders.addAll("fabric", "quilt")
	syncBodyFrom.set(rootProject.file("README.md").readText())
	dependencies {
		required.version("fabric-api", libs.versions.fabric.api.get())
	}
}
