# WindowsDesktopInternetLinkBuilder
Small java application that creates .url files in a Windows 10 Desktop folder

## WARNING
The /user/$USER/Desktop directory is read-only. As a result of that, for this application to work, you must right-click on the Desktop folder, choose properties, then using the Location tab, select a custom location for the Desktop folder.  If anyone knows a way around this requirement, please let me know, because anytime I try to write a file directly in /user/$USER/Desktop I get an "access denied" or "file not found" error.
