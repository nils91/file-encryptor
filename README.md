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

## File format
The file-encryptor uses to different types of files: The AES-Keyfile (see options -k and -w), and the encrypted file. Starting with pre-release version 0.0.4 the internal structure of those files will be changed.

### Up to and including 0.0.3
The structure of the files is quite simple. The AES keyfile, which has the ending '.key' by default, is a binary file containing the bytes that make up the key. For AES-256 the key is 32 bytes in size. The keyfile only contains the key and nothing else.
The encrypted file is also very simple. It too is a binary file and contains the IV (initial vector) and the encrypted file. The first byte is the size of the IV (16, or 0x10 in hex), followed by the IV and then the encrypted file.

### 