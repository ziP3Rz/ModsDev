package net.zip3rz.testmod.datagen;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.zip3rz.testmod.TestMod;
import net.zip3rz.testmod.block.ModBlocks;
import net.zip3rz.testmod.block.custom.TuberiaBlock;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModBlockStateProvider extends BlockStateProvider {

    List<String> locationSuffixes = List.of("base_pipe", "corner_pipe", "t_pipe");
    Map<String, ModelFile> modelFiles = new HashMap<>();
    ModelFile modelFile1 ;

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, TestMod.MOD_ID, exFileHelper);
        for (String suffix : locationSuffixes) {
            ResourceLocation location = new ResourceLocation("testmod", "block/" + suffix);
            modelFiles.put(suffix, new ModelFile.ExistingModelFile(location, exFileHelper));
        }
    }

    @Override
    protected void registerStatesAndModels() {
        MultiPartBlockStateBuilder builder = this.getMultipartBuilder(ModBlocks.TUBERIA.get());

        builder    // Bloque por defecto: Numero de vecinos = 0
            .part()
                .modelFile(this.modelFiles.get("base_pipe"))
                .addModel()
                    .condition(TuberiaBlock.NEIGHBOURS, 0)
            .end()

            // Numero de vecinos = 1
            .part()
                .modelFile(this.modelFiles.get("base_pipe"))
                .addModel()
                .useOr()
                    .nestedGroup()
                        .condition(TuberiaBlock.NEIGHBOURS, 1)
                        .condition(TuberiaBlock.NORTH, true)
                    .end()
                    .nestedGroup()
                        .condition(TuberiaBlock.NEIGHBOURS, 1)
                        .condition(TuberiaBlock.SOUTH, true)
                    .end()
                .end();

        builder
            .part()
                .modelFile(this.modelFiles.get("base_pipe"))
                .rotationY(90)
                .addModel()
                .useOr()
                    .nestedGroup()
                        .condition(TuberiaBlock.NEIGHBOURS, 1)
                        .condition(TuberiaBlock.WEST, true)
                    .end()
                    .nestedGroup()
                        .condition(TuberiaBlock.NEIGHBOURS, 1)
                        .condition(TuberiaBlock.EAST, true)
                    .end()
            .end()

            .part()
                .modelFile(this.modelFiles.get("base_pipe"))
                .rotationX(90)
                .addModel()
                .useOr()
                    .nestedGroup()
                        .condition(TuberiaBlock.NEIGHBOURS, 1)
                        .condition(TuberiaBlock.UP, true)
                    .end()
                    .nestedGroup()
                        .condition(TuberiaBlock.NEIGHBOURS, 1)
                        .condition(TuberiaBlock.DOWN, true)
                    .end()
                .end();


            // Numero de vecinos = 2
                //Vecinos en el mismo eje
        builder
           .part()
                .modelFile(this.modelFiles.get("base_pipe"))
                .rotationX(90)
                .addModel()
                    .condition(TuberiaBlock.NEIGHBOURS, 2)
                    .condition(TuberiaBlock.UP, true)
                    .condition(TuberiaBlock.DOWN, true)
            .end()

            .part()
                .modelFile(this.modelFiles.get("base_pipe"))
                .addModel()
                    .condition(TuberiaBlock.NEIGHBOURS, 2)
                    .condition(TuberiaBlock.NORTH, true)
                    .condition(TuberiaBlock.SOUTH, true)
            .end()

            .part()
                .modelFile(this.modelFiles.get("base_pipe"))
                .rotationY(90)
                .addModel()
                    .condition(TuberiaBlock.NEIGHBOURS, 2)
                    .condition(TuberiaBlock.EAST, true)
                    .condition(TuberiaBlock.WEST, true)
            .end()

                // Vecinos en diferentes ejes
                    // Con la esquina para 2 vertical
                        //DOWN
            .part()
                .modelFile(this.modelFiles.get("corner_pipe"))
                .rotationX(0)
                .rotationY(0)
                .addModel()
                    .condition(TuberiaBlock.NEIGHBOURS, 2)
                    .condition(TuberiaBlock.DOWN, true)
                    .condition(TuberiaBlock.EAST, true)
            .end()

            .part()
                .modelFile(this.modelFiles.get("corner_pipe"))
                .rotationX(0)
                .rotationY(90)
                .addModel()
                    .condition(TuberiaBlock.NEIGHBOURS, 2)
                    .condition(TuberiaBlock.DOWN, true)
                    .condition(TuberiaBlock.SOUTH, true)
            .end()

            .part()
                .modelFile(this.modelFiles.get("corner_pipe"))
                .rotationX(0)
                .rotationY(180)
                .addModel()
                    .condition(TuberiaBlock.NEIGHBOURS, 2)
                    .condition(TuberiaBlock.DOWN, true)
                    .condition(TuberiaBlock.WEST, true)
            .end()

            .part()
                .modelFile(this.modelFiles.get("corner_pipe"))
                .rotationX(0)
                .rotationY(270)
                .addModel()
                    .condition(TuberiaBlock.NEIGHBOURS, 2)
                    .condition(TuberiaBlock.DOWN, true)
                    .condition(TuberiaBlock.NORTH, true)
            .end()

                        //UP
            .part()
                .modelFile(this.modelFiles.get("corner_pipe"))
                .rotationX(180)
                .addModel()
                    .condition(TuberiaBlock.NEIGHBOURS, 2)
                    .condition(TuberiaBlock.UP, true)
                    .condition(TuberiaBlock.EAST, true)
            .end()

            .part()
                .modelFile(this.modelFiles.get("corner_pipe"))
                .rotationX(180)
                .rotationY(90)
                .addModel()
                    .condition(TuberiaBlock.NEIGHBOURS, 2)
                    .condition(TuberiaBlock.UP, true)
                    .condition(TuberiaBlock.SOUTH, true)
            .end()

            .part()
                .modelFile(this.modelFiles.get("corner_pipe"))
                .rotationX(180)
                .rotationY(180)
                .addModel()
                    .condition(TuberiaBlock.NEIGHBOURS, 2)
                    .condition(TuberiaBlock.UP, true)
                    .condition(TuberiaBlock.WEST, true)
            .end()

            .part()
                .modelFile(this.modelFiles.get("corner_pipe"))
                .rotationX(180)
                .rotationY(270)
                .addModel()
                    .condition(TuberiaBlock.NEIGHBOURS, 2)
                    .condition(TuberiaBlock.UP, true)
                    .condition(TuberiaBlock.NORTH, true)
            .end()

                    //Con la esquina para dos horizontal
            .part()
                .modelFile(this.modelFiles.get("corner_pipe"))
                .rotationX(90)
                .addModel()
                    .condition(TuberiaBlock.NEIGHBOURS, 2)
                    .condition(TuberiaBlock.SOUTH, true)
                    .condition(TuberiaBlock.EAST, true)
            .end()

            .part()
                .modelFile(this.modelFiles.get("corner_pipe"))
                .rotationX(90)
                .rotationY(90)
                .addModel()
                    .condition(TuberiaBlock.NEIGHBOURS, 2)
                    .condition(TuberiaBlock.SOUTH, true)
                    .condition(TuberiaBlock.WEST, true)
            .end()

            .part()
                .modelFile(this.modelFiles.get("corner_pipe"))
                .rotationX(90)
                .rotationY(180)
                .addModel()
                    .condition(TuberiaBlock.NEIGHBOURS, 2)
                    .condition(TuberiaBlock.NORTH, true)
                    .condition(TuberiaBlock.WEST, true)
            .end()

            .part()
                .modelFile(this.modelFiles.get("corner_pipe"))
                .rotationX(90)
                .rotationY(270)
                .addModel()
                    .condition(TuberiaBlock.NEIGHBOURS, 2)
                    .condition(TuberiaBlock.NORTH, true)
                    .condition(TuberiaBlock.EAST, true)
            .end();


        addHorizontalVariationParts(builder, "t_pipe", 3, 0, true, true, true);
        addHorizontalVariationParts(builder, "t_pipe", 3, 90, true, false, false);
            // Numero de vecinos = 3
                // Vecinos en el mismo plano
                    //Vertical
        /* builder
            .part()
                .modelFile(this.modelFiles.get("t_pipe"))
                .rotationX(0)
                .rotationY(0)
                .addModel()
                    .condition(TuberiaBlock.NEIGHBOURS, 3)
                    .condition(TuberiaBlock.DOWN, true)
                    .condition(TuberiaBlock.UP, true)
                    .condition(TuberiaBlock.NORTH, true)
            .end()

            .part()
                .modelFile(this.modelFiles.get("t_pipe"))
                .rotationX(0)
                .rotationY(90)
                .addModel()
                    .condition(TuberiaBlock.NEIGHBOURS, 3)
                    .condition(TuberiaBlock.DOWN, true)
                    .condition(TuberiaBlock.UP, true)
                    .condition(TuberiaBlock.EAST, true)
            .end()

            .part()
                .modelFile(this.modelFiles.get("t_pipe"))
                .rotationX(0)
                .rotationY(180)
                .addModel()
                    .condition(TuberiaBlock.NEIGHBOURS, 3)
                    .condition(TuberiaBlock.DOWN, true)
                    .condition(TuberiaBlock.UP, true)
                    .condition(TuberiaBlock.SOUTH, true)
            .end()

            .part()
                .modelFile(this.modelFiles.get("t_pipe"))
                .rotationX(0)
                .rotationY(270)
                .addModel()
                    .condition(TuberiaBlock.NEIGHBOURS, 3)
                    .condition(TuberiaBlock.DOWN, true)
                    .condition(TuberiaBlock.UP, true)
                    .condition(TuberiaBlock.WEST, true)
            .end()

                        //Horizontal
            .part()
                .modelFile(this.modelFiles.get("t_pipe"))
                .rotationX(90)
                .addModel()
                    .condition(TuberiaBlock.NEIGHBOURS, 3)
                    .condition(TuberiaBlock.NORTH, true)
                    .condition(TuberiaBlock.EAST, true)
                    .condition(TuberiaBlock.SOUTH, true)
            .end()

            .part()
                .modelFile(this.modelFiles.get("t_pipe"))
                .rotationX(90)
                .rotationY(90)
                .addModel()
                    .condition(TuberiaBlock.NEIGHBOURS, 3)
                    .condition(TuberiaBlock.EAST, true)
                    .condition(TuberiaBlock.SOUTH, true)
                    .condition(TuberiaBlock.WEST, true)
            .end()

            .part()
                .modelFile(this.modelFiles.get("t_pipe"))
                .rotationX(90)
                .rotationY(180)
                .addModel()
                    .condition(TuberiaBlock.NEIGHBOURS, 3)
                    .condition(TuberiaBlock.SOUTH, true)
                    .condition(TuberiaBlock.WEST, true)
                    .condition(TuberiaBlock.NORTH, true)
            .end()

            .part()
                .modelFile(this.modelFiles.get("t_pipe"))
                .rotationX(90)
                .rotationY(270)
                .addModel()
                    .condition(TuberiaBlock.NEIGHBOURS, 3)
                    .condition(TuberiaBlock.WEST, true)
                    .condition(TuberiaBlock.NORTH, true)
                    .condition(TuberiaBlock.EAST, true)
            .end()
        ; */


        blockWithItem(ModBlocks.CUBE);
    }


    // public MultiPartBlockStateBuilder getPipeMultipartBuilder

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void addHorizontalVariationParts(MultiPartBlockStateBuilder builder, String model, int numberNeighbours,
                                            int rotationX, boolean horizontalCorners, boolean up, boolean down) {

        Direction[] directions = new Direction[]{Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH};

        int numberVerticalNeighbours = (up ? 1: 0) + (down ? 1 : 0);
        // El número de vecinos horizontales será un numero entero entre 0 y 4.
        int numberHorizontalNeighbours = numberNeighbours - numberVerticalNeighbours;
        if (numberHorizontalNeighbours > directions.length ) {
            System.out.println("Algo va mal");
        }
        // El número de rotaciones será 1 si es número de vecinos horizontales es 0 o 4 y 4 en otro caso.
        int numberRotations = (numberHorizontalNeighbours % 4 == 0) ? 1 : 4;

        // TODO: La array de booleans para 3 horizontales está mal
        // Creamos una lista que almacene los valores true o false de vecinos en las direcciones EAST, SOUTH, WEST, NORTH en dicho ordem.
        List<Boolean> horizontalNeighboursStates = new ArrayList<Boolean>();
        for (int i = 0; i < directions.length; i++) {
            if (horizontalCorners) {
                if (i < numberHorizontalNeighbours) {
                    horizontalNeighboursStates.add(true);
                } else {
                    horizontalNeighboursStates.add(false);
                }
            } else {
                numberRotations = 2;
                if (i % numberRotations == 0) {
                    horizontalNeighboursStates.add(true);
                } else {
                    horizontalNeighboursStates.add(false);
                }
            }
        }

        for (int i = 0; i < numberRotations; i++) {
            builder
                .part()
                    .modelFile(this.modelFiles.get(model))
                    .rotationX(rotationX)
                    .rotationY(90*i)
                    .addModel()
                        .condition(TuberiaBlock.NEIGHBOURS, numberNeighbours)
                        .condition(TuberiaBlock.UP, up)
                        .condition(TuberiaBlock.DOWN, down)
                        .condition(TuberiaBlock.EAST, horizontalNeighboursStates.get(i%4))
                        .condition(TuberiaBlock.SOUTH, horizontalNeighboursStates.get((i+1)%4))
                        .condition(TuberiaBlock.WEST, horizontalNeighboursStates.get((i+2)%4))
                        .condition(TuberiaBlock.NORTH, horizontalNeighboursStates.get((i+3)%4))
                .end();
        }
    }

}
