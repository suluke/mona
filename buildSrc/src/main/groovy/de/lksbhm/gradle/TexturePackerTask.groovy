package de.lksbhm.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.incremental.IncrementalTaskInputs
import com.badlogic.gdx.tools.texturepacker.TexturePacker

class TexturePackerTask extends DefaultTask {
	@Optional
	@Input
	String packName = "pack"
	
	@Input
	@InputDirectory
	def File inputDir
	
	@Input
	def File outputDir
	
	@OutputFile
	File getOutputFile() {
		return new File(outputDir.getCanonicalPath() + File.separatorChar + packName + '.atlas')
	}

	@TaskAction
	void execute(IncrementalTaskInputs inputs) {
		println inputs.incremental ? "CHANGED inputs considered out of date" : "ALL inputs considered out of date"
		project.ant.delete (includeEmptyDirs: 'true') {
			fileset(dir: outputDir, includes: packName + '.*') 
		} 
		TexturePacker.main([inputDir, outputDir, packName] as String[])
		
		inputs.outOfDate { change ->
			println "out of date: ${change.file.name}"
		}

		inputs.removed { change ->
			println "removed: ${change.file.name}"
		}
	}
}
