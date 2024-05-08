:: Stop the repl if it is running since it will prevent the new files from being copied into the directory and wait a second for resources to be released
call "0-KillPotion.bat"
TIMEOUT /T 1


::Remove directory and recreate it
set dstDir="C-Sharp-Libs\"
rmdir %dstDir% /s /q
mkdir %dstDir%


::Copy all files from the individual C# projects
xcopy   "..\\ConnectToProgram\\bin\\Release\\net7.0-windows\\"            %dstDir% /y /E /C
xcopy   "..\\ICControllerLibraries\\ControllerLibrary\\bin\\Release\\net7.0-windows\\"           %dstDir% /y /E /C
xcopy   "..\\NamedPipeStreamServerThread\\bin\\Release\\net7.0-windows\\" %dstDir% /y /E /C