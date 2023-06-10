set PATH_TO_FX="C:\Program Files\Java\javafx-sdk-17.0.7"
set PATH_TO_JAVA_MODULES="C:\Program Files\Java\modules"
java ^
--module-path jars/client-1.0-SNAPSHOT.jar;%PATH_TO_FX%;%PATH_TO_FX%\lib;%PATH_TO_JAVA_MODULES% ^
--module ru.hse.javaprogramming.client/ru.hse.javaprogramming.client.KlavogonkiApplication
