# Algorithms-and-Analysis
Assignment 1 for Algorithms and Analysis RMIT

This assignment was meant to expose us to the difference in efficiency and time complexity between brute force algorithms and KD Tree

In order to run this program
Use CLI to compile the files at src folder level
  javac *.java
  
In order to run the program follow the following instructions

  java NearestNeighFileBased [approach] [data filename] [command filename] [output filename]
  
  where
    approach is one of "naive" or "kdtree";
    
    data filename is the name of the file containing the initial set of points
      in this case its sampleData.txt
      
    command filename is the name of the file with the commands/operations
      this can be found under testing folder (any files ending with .in)
      
    output filename is where to store the output of program
      can be named based on user preference
      
  If you would like to automate the testing process, the following CLI command will help you do so
    python assign1TestScript.py [-v] <codeDirectory> <name of implementation to test> <data filename> <list of input files to test on>
    
 assign1.pdf details the report on the experiement conducted on to compare the time efficiency between the two implementations.
