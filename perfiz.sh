#! /bin/sh
if [ -z "${PERFIZ_HOME}" ]; then
  echo 'Please set PERFIZ_HOME environment variable. Exiting.';
  exit 1;
fi
if [ ! -d "${PERFIZ_HOME}" ]; then
  echo "PERFIZ_HOME is set to a directory that does not exist: $PERFIZ_HOME. Exiting.";
  exit 1;
fi
if [ ! -e "$PERFIZ_HOME/perfiz-cli" ]; then
  "Initial Setup. Downloading perfiz-cli."
  OS=`uname -s`
  ARCH=`uname -m`
  echo "Detecting OS and Architecture $OS-$ARCH"
  PERFIZ_EXECUTABLE=`curl -s https://api.github.com/repos/znsio/perfiz-cli/releases/latest \
  | grep "browser_download_url.*perfiz-cli/releases.*_$OS_$ARCH.tar.gz" \
  | grep "$OS" \
  | cut -d ":" -f 2,3 \
  | tr -d \"`
  PERFIZ_EXECUTABLE=`echo $PERFIZ_EXECUTABLE | sed -e "s/^M//"`
  echo "$PERFIZ_EXECUTABLE"
  case "$OS" in
    (Darwin)
      curl -L "$PERFIZ_EXECUTABLE" --output "$PERFIZ_HOME/perfiz-cli.tar.gz"
      tar -xvf "$PERFIZ_HOME/perfiz-cli.tar.gz" -C "$PERFIZ_HOME"
      chmod +x "$PERFIZ_HOME/perfiz-cli"
      xattr -d com.apple.quarantine "$PERFIZ_HOME/perfiz-cli"
      ;;
    (Linux)
      wget -O "$PERFIZ_HOME/perfiz-cli.tar.gz" "$PERFIZ_EXECUTABLE"
      tar -xvf "$PERFIZ_HOME/perfiz-cli.tar.gz" -C "$PERFIZ_HOME"
      chmod +x "$PERFIZ_HOME/perfiz-cli"
      ;;
    (*)
      ;;
  esac
fi

$PERFIZ_HOME/perfiz-cli $@