#!/bin/bash
set -u

abort() {
  echo "%s\n" "$@"
  exit 1
}

if [ -z "${BASH_VERSION:-}" ]; then
  abort "Bash is required to interpret this script."
fi

if [ -z "${PERFIZ_HOME}" ]; then
  abort "Please set PERFIZ_HOME environment variable. Exiting."
fi

if [ ! -d "${PERFIZ_HOME}" ]; then
  abort "PERFIZ_HOME is set to a directory that does not exist: $PERFIZ_HOME. Exiting."
fi

if [ ! -e "$PERFIZ_HOME/.VERSION" ]; then
  abort "$PERFIZ_HOME/.VERSION does not exist. Please check if you have set PERFIZ_HOME correctly."
fi

CURRENT_VERSION=`cat $PERFIZ_HOME/.VERSION`
echo "Current Version: $CURRENT_VERSION"

LATEST_VERSION=`curl -s https://api.github.com/repos/znsio/perfiz/releases/latest | grep '"tag_name":' | cut -d ":" -f2 | tr -d \" | tr -d \, | tr -d \ `

if [[ $CURRENT_VERSION == $LATEST_VERSION ]]; then
  echo "Already up to date"
  exit 0
fi

echo "Latest Version: $LATEST_VERSION"

OS=`uname -s`

PERFIZ_ZIP=`curl -s https://api.github.com/repos/znsio/perfiz/releases/latest | grep "browser_download_url.*perfiz" | cut -d ":" -f 2,3 | tr -d \" | tr -d \ `
echo "Downloading $PERFIZ_ZIP"

case "$OS" in
  (Darwin)
    curl -L "$PERFIZ_ZIP" --output "$PERFIZ_HOME/perfiz.zip"
    ;;
  (Linux)
    wget -O "$PERFIZ_HOME/perfiz.zip" "$PERFIZ_ZIP"
    ;;
  (*)
    echo "$OS not supported"
    ;;
esac

if [ ! -e "$PERFIZ_HOME/perfiz.zip" ]; then
  abort "Error downloading latest versin. $PERFIZ_HOME/perfiz.zip does not exist."
fi

echo "Installing"
cd $PERFIZ_HOME
ls -a $PERFIZ_HOME | grep -v perfiz.zip | grep -v total | grep -v .m2 | grep -v '^.$' | grep -v '^..$' | xargs rm -fR
unzip "$PERFIZ_HOME/perfiz.zip" -C "$PERFIZ_HOME"
rm "$PERFIZ_HOME/perfiz.zip"

echo "Done..."