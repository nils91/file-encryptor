# File Encryptor
Encrypt/decrypt files with AES.

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
 -t,--verbose          Switch on verbode mode
 -v,--version          Show version information
 -w,--writekey <arg>   Write the key to the console (or a file) once
                       operation is complete. The argument is optional. If
                       the argument is 'yes', the key will be written to
                       a filename generated from the output file name.
                       Alternativly a filepath can be provided.
```