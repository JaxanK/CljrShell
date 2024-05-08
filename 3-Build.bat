call potion -m build


for /d %%a in (".\target\*") do (
    set "firstSubFolder=%%~nxa"
    rem Store the first subfolder name and exit the loop
    goto :SubFolderFound
)
:SubFolderFound



set "targetDst=.\target\%firstSubFolder%"

::Copy specific from this directory to destination
xcopy   ".\\src"                           "%targetDst%\src\"            /y /E /C
xcopy   ".\\obj"                           "%targetDst%\obj\"            /y /E /C
xcopy   ".\\C-Sharp-Libs"                  "%targetDst%\C-Sharp-Libs\"   /y /E /C
xcopy   "*-RunLocal.bat"                   "%targetDst%"                 /y
xcopy   "*-KillPotion.bat"                 "%targetDst%"                 /y
xcopy   "CljrShell.csproj"            "%targetDst%"                 /y
xcopy   "ControllerIconSystemTray-Red.ico" "%targetDst%"                 /y
xcopy   ".nrepl-port"                      "%targetDst%"                 /y

