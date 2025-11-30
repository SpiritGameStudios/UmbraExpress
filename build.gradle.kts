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
	mavenLocal()
	mavenCentral()
	maven {
		name = "Spirit Studios Releases"
		url = uri("https://maven.spiritstudios.dev/releases/")

		content {
			@Suppress("UnstableApiUsage")
			includeGroupAndSubgroups("dev.spiritstudios")
		}
	}
	maven {
		name = "Modrinth"
		url = uri("https://api.modrinth.com/maven")
		content {
			includeGroup("maven.modrinth")
		}
	}
	maven {
		name = "Mod Menu"
		url = uri("https://maven.terraformersmc.com/releases/")
	}
	// CCA, Ratatouille
	maven {
		name = "Ladysnake Mods"
		url = uri("https://maven.ladysnake.org/releases")
	}
	maven {
		name = "Datasync"
		url = uri("https://maven.uuid.gg/releases")
	}
	maven {
		url = uri("https://maven.maxhenkel.de/repository/public")
	}
}


fabricApi {
	configureDataGeneration {
		client = true
		modId = "umbra_express"
	}
}

dependencies {
	minecraft(libs.minecraft)
	mappings(variantOf(libs.yarn) { classifier("v2") })
	modImplementation(libs.fabric.loader)

	modImplementation(libs.fabric.api)

	modImplementation(libs.trainmurdermystery)

	modImplementation(libs.ratatouille)

	modImplementation(libs.cca.base)
	modImplementation(libs.cca.world)
	modImplementation(libs.cca.entity)
	modImplementation(libs.cca.scoreboard)

	modRuntimeOnly(libs.modmenu)

	modImplementation(libs.voicechat.api)
	modImplementation(libs.voicechat)
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
