# File Encryptor
file-encyptor is a small standalone tool that lets you encrypt/decrypt files using AES-256. The tool is used on the command line. An installed JRE/JDK is required.

To build from source:\
`mvn clean install`\
The executable jar is called `file-encryptor-[VERSION]-shaded.jar`.\
To run the tests:\
`mvn clean test`

Minimal encryption example:\
`java -jar file-encryptor.jar -e -i path/to/input.file -w`\
Minimal decryption example:\
`java -jar file-encryptor.jar -d -i path/to/input.file.enc -k <key>`

```
Usage: file-encryptor
 -d,--decrypt          This flag sets the application to decryption mode
 -e,--encrypt          This flag sets the application to encryption mode
 -h,--help             Show usage instructions
 -i,--in <arg>         Input file
 -k,--key <arg>        Encrypt/Decrypt using a key. Must be encoded in
                       Base64 when read from console.
 -o,--out <arg>        Output file name. If this parameter is not given,
                       the output filename in encryption mode is just the
                       input filename + '.enc' or '.dec' in decryption
                       mode.
 -r,--remove           Remove the input file after the operation is
                       complete
 -t,--verbose          Switch on verbode mode
 -v,--version          Show version information
 -w,--writekey <arg>   Write the key to the console (or a file) once
                       operation is complete. The argument is optional. If
                       the argument is 'yes', the key will be written to
                       a filename generated from the output file name.
                       Alternativly a filepath can be provided.
```