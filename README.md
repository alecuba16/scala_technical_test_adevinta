# Adevinta Scala technical test for Senior Data Engineer
## Algorithm
I have decided to use a trie data structure for faster searching since a trie has a lookup time of O(1) in most cases. A trie is a tree-like structure where each node has as many children as the alphabet that it considers. In this case, I have considered as the alphabet the ASCII codes from '0' char code 48 to 'z', which is 122, then each node of the tree has 122-48 = 74 children. The algorithm decides the children's position by the char code of the input word. Then the tree is constructed like a->[abc...z] aa->[abc..z] aaa=>[abc..z].

A possible calculation is the size of the alphabet (a) * avg word length (w) * number of words (N), O(awN) -> O(N). Time complexity search is O(1), because we can get the result in one pass, insertion is O(N) because we have to visit N nodes.
Consulted info for the code: https://en.wikipedia.org/wiki/Trie

## Implementation 
The implementation was done inside the TrieDict case class (supporting the data structure) and a companion object for managing the data. The implementation is recursive as it is the easiest way to run over a tree.

I have separated the processing of the input files (inside FileProcessor class) from the SearchIndex class, which holds the different dictionaries that were generated when the program launched.

## Build
Just run the `sbt assembly` to create a fat jar in the directory `target/scala-2.12/` with the name 
`Alejandro Blanco-M-assembly-0.1.0-SNAPSHOT.jar`

## Run
To run the code just launch the fat jar with the included text folder (input_texts) with the command `java -jar Alejandro Blanco-M-assembly-0.1.0-SNAPSHOT.jar ./input_texts`

To quit the program, just enter `:quit`
