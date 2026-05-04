Add-Type -AssemblyName System.Drawing
$img = [System.Drawing.Image]::FromFile("d:\applications\IDEA\IntelliJ IDEA\IDEA_code\big_competition\resources\image\太和殿.png")
Write-Host "Width: $($img.Width), Height: $($img.Height)"
$img.Dispose()
