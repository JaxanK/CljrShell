# A Clojure CLR Shell for running .NET interactions from Clojure on the JVM

A simple wrapper program that will setup a NamedPipe Server to read and evaluate
code sent from Clojure on the JVM.

If communicating with other dotnet programs, you may need to add additional
files and libraries to this template. 

To connect from Clojure on the JVM use something like the below code

```Clojure
  (defn- CommSendMsgNewCljrShell [messageStr]
    (def PipeString "\\\\.\\pipe\\CljrShellServerIncoming")
    (try
      (def testpipe (new java.io.RandomAccessFile PipeString "rw"))

      (let [messageStr (if (not (str/ends-with? messageStr "\n")) (str messageStr "\n") messageStr)]
        (. testpipe write (. messageStr getBytes)))

          ;return
      (. testpipe readLine)

      (catch Exception e
        (if (str/includes? (.getMessage e) "All pipe instances are busy")
          (do (println "Server Instances Busy, Delaying and then will try again")
            (Thread/sleep 500)
            (CommSendMsgNewCljrShell messageStr))))))

  (defn sendToCLR [lsp]
    (if (string? lsp)
      (let [response (CommSendMsgNewCljrShell lsp)]
        (try (load-string response) (catch Exception _ response)))
      nil))



    ;Functions made with inspiration from Clojure Electric in how data is processed. 
    ; These are incomplete but work to evaluate code, only issue is data passthrough is incomplete as only works for static AOT compile values.
  (defn CljProgram [form]
    (if (seq? form)
      (let [[head & tail] form]
        (if (and (symbol? head) (= CljProgram (try (eval head) (catch Exception e nil)))) ; Have to wrap in try statement because if symbol only exists in  it will throw error
          (eval (first tail))
          (cons (CljProgram head) (map CljProgram tail))))
      form))

  (defmacro CljrShell [& code]
    `(sendToCLR ~(reduce str (apply prn-str (CljProgram code)))))

    ```