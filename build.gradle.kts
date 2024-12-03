plugins {
    alias(libs.plugins.fabric.loom)
}

version = project.property("mod_version") as String
group = project.property("maven_group") as String

base {
    archivesName.set(project.property("archives_base_name") as String)
}

val targetJavaVersion = 17
java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
}

loom {
//    accessWidenerPath = file("src/main/resources/darkdaysahead.accesswidener")

    mods {
        register("mcssc") {
            sourceSet("main")
        }
    }
}

repositories {
    exclusiveContent {
        forRepository {
            maven {
                name = "Modrinth"
                url = uri("https://api.modrinth.com/maven")
            }
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }
    maven("https://maven.bawnorton.com/releases")
    maven("https://maven.shedaniel.me")
    maven("https://maven.terraformersmc.com/releases")
}

dependencies {
    minecraft(libs.com.mojang.minecraft)
    mappings(variantOf(libs.net.fabricmc.yarn) { classifier("v2") })
    modImplementation(libs.net.fabricmc.fabric.loader)
    libs.com.github.bawnorton.mixinsquared.fabric.run {
        annotationProcessor(this)
        implementation(this)
        include(this)
    }

    modCompileOnly(libs.modrinth.sereneseasons)
}

tasks.processResources {
    val properties = mapOf(
        "id" to project.property("mod_id") as String,
        "version" to project.version,
        "name" to project.property("mod_name") as String,
        "minecraft_version" to libs.versions.minecraft.get(),
        "loader_version" to libs.versions.fabric.loader.get()
    )
    filteringCharset = "UTF-8"
    inputs.properties(properties)
    filesMatching("fabric.mod.json") { expand(properties) }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(targetJavaVersion)
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.base.archivesName}" }
    }
}
