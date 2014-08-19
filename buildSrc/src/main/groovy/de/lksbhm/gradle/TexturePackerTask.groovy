package de.lksbhm.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.incremental.IncrementalTaskInputs
import com.badlogic.gdx.tools.texturepacker.TexturePacker

class TexturePackerTask extends DefaultTask {
    @InputDirectory
    def File inputDir

    @OutputDirectory
    def File outputDir

    @TaskAction
    void execute(IncrementalTaskInputs inputs) {
        println inputs.incremental ? "CHANGED inputs considered out of date" : "ALL inputs considered out of date"
        TexturePacker.main([inputDir, outputDir] as String[])
        
        inputs.outOfDate { change ->
            println "out of date: ${change.file.name}"
        }

        inputs.removed { change ->
            println "removed: ${change.file.name}"
        }
    }
}
