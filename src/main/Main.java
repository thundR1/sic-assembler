/**
 * Fall 2021-2022
 * Systems Programming Course Project.
 * Computer Engineering Department
 * 
 * @file This File Contains The Main Class And
 *       is Resposible for Initializing The
 *       Assembler and Main Components.
 * 
 * Group: {
 *     - Youssef Elsayed Abdelkader Nasr Amin { 201900314 }
 *     - Ibrahim Elsayed Abdelkader Nasr Amin { 201901434 }
 *     - Mohamed Abdelwahab Elmorsi Ali Ramah { 201900307 }
 *     - Mina Adel Gad Moawad                 { 201901470 }
 * }
*/
package main;

/** Internal Imports */
import analyzers.Assembler;
import data.tabulation.Table;
import data.handlers.FileHandler;

public class Main {
    public static void main(String[] args) {

        /** Dynamically Obtain Current Working Directory */
        final String currentWorkingDirectory = System.getProperty("user.dir");

        /** Creating Custom File Handler Object Over The 'instructions.asm' file */
        FileHandler sourceCode = new FileHandler(currentWorkingDirectory + "/instructions.asm");

        /**
         * Creating New SIC Assembler Object Over The 'sourceCode' FileHandler.
         * 
         * @note Assembler Type is a SIC Assembler For Virtual
         *       SIC Machines And Not For Modern Ones Like x86
         *       Assembly Machines.
         * 
         * @param sourceCode - FileHandler
        */
        Assembler asm = new Assembler(sourceCode);

        /** Create New Table Object Used For Constructing Output Tables */
        Table table = new Table(sourceCode, currentWorkingDirectory);
        table.tableAll(asm);
    }
}
