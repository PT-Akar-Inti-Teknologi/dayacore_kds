package app.dayacore.kds.core.dummy

object HtmlRes {
    val html =
        """
        <html>
        <head>
            <title>Compose WebView Multiplatform</title>
            <style>
                body {
                    background-color: e0e8f0; 
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    flex-direction: column;
                    height: 100vh; 
                    margin: 0;
                }
                h1, h2 {
                    text-align: center; 
                    color: ffffff; 
                }
                @media (prefers-color-scheme: dark) {
                  body {
                    background-color: white;
                  }
                  h1, h2 {
                    color: black; 
                  }
                }
            </style>
        </head>
        <body>
            <script type="text/javascript">
                function callJS() {
                    return 'Response from JS';
                }
                function callDesktop() {
                    window.cefQuery({
                            request: "1_callDesktop_{\"message\":\"1\"}",
                            onSuccess: function(response) {
                                // 处理Java应用程序的响应
                            },
                            onFailure: function(errorCode, errorMessage) {
                                // 处理错误
                            }
                        });
                }
                function callGetKdsSettings() {
                    window.kmpJsBridge.callNative("GetKdsSettings", JSON.stringify({message: "Hello"}),
                            function (data) {
                                document.getElementById("get").innerText = data;
                                console.log("KDS Settings get : " + data);
                            }
                        );
                }
                function callSaveKdsSettings() {
                    window.kmpJsBridge.callNative("SaveKdsSettings", JSON.stringify({"view_mode":"Kitchen / Bar","visit_purpose_id":[],"time_first_warning":10,"time_second_warning":20,"stations_id":[],"printing_station_id":[]}),
                            function (data) {
                                document.getElementById("save").innerText = data;
                                console.log("KDS Settings save : " + data);
                            }
                        );
                }
                function callResetKdsSettings() {
                    window.kmpJsBridge.callNative("SaveKdsSettings", JSON.stringify({"view_mode":"","visit_purpose_id":[],"time_first_warning":5,"time_second_warning":10,"stations_id":[],"printing_station_id":[]}),
                            function (data) {
                                document.getElementById("reset").innerText = data;
                                console.log("KDS Settings reset : " + data);
                            }
                        );
                }
            </script>
            <h1>Compose WebView Multiplatform</h1>
            <h2 id="get">Get</h2>
            <button onclick="callGetKdsSettings()">GetKdsSettings</button>
            <h2 id="save">Save</h2>
            <button onclick="callSaveKdsSettings()">SaveKdsSettings</button>
            <h2 id="reset">Reset</h2>
            <button onclick="callResetKdsSettings()">ResetKdsSettings</button>
        </body>
        </html>
        """.trimIndent()
}
