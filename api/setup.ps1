# Create package directories
$packages = @(
    "controller",
    "service",
    "repository",
    "model",
    "exception"
)

$basePath = "src/main/java/com/library/api"

foreach ($package in $packages) {
    $path = Join-Path $basePath $package
    New-Item -ItemType Directory -Path $path -Force
} 