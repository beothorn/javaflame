var executionMetadata = "some arguments";

var data = [[
{"thread":"main","snapshotTime":1698585698020,"span":{"name":"mainRoot","entryTime":1698585697056,"exitTime":1698585697452,"value":396,"children":[{"name":"com.github.beothorn.App.main([Ljava.lang.String; arg0 = [])","entryTime":1698585697056,"exitTime":1698585697452,"value":396,"children":[{"name":"com.github.junrar.Junrar.extract(java.lang.String arg0 = /tmp/foo.rar, java.lang.String arg1 = /tmp)","entryTime":1698585697254,"exitTime":1698585697452,"value":198,"children":[{"name":"com.github.junrar.Junrar.validateRarPath(java.io.File arg0 = /tmp/foo.rar)","entryTime":1698585697452,"exitTime":1698585697452,"value":0}
]}
]}
]}
},{"thread":"Thread-2","snapshotTime":1698585698054,"span":{"name":"Thread-2Root","entryTime":1698585697061,"exitTime":-1,"value":0,"children":[{"name":"com.github.beothorn.App.printNumber(int arg0 = 0)","entryTime":1698585697061,"exitTime":1698585697261,"value":200}
,{"name":"com.github.beothorn.App.printNumber(int arg0 = 1)","entryTime":1698585697270,"exitTime":1698585697470,"value":200}
,{"name":"com.github.beothorn.App.printNumber(int arg0 = 2)","entryTime":1698585697470,"exitTime":1698585697670,"value":200}
,{"name":"com.github.beothorn.App.printNumber(int arg0 = 3)","entryTime":1698585697670,"exitTime":1698585697871,"value":201}
]}
}
],
[
{"thread":"Thread-2","snapshotTime":1698585699057,"span":{"name":"Thread-2Root","entryTime":1698585697061,"exitTime":-1,"value":0,"children":[{"name":"com.github.beothorn.App.printNumber(int arg0 = 4)","entryTime":1698585697871,"exitTime":1698585698071,"value":200}
,{"name":"com.github.beothorn.App.printNumber(int arg0 = 5)","entryTime":1698585698071,"exitTime":1698585698271,"value":200}
,{"name":"com.github.beothorn.App.printNumber(int arg0 = 6)","entryTime":1698585698272,"exitTime":1698585698472,"value":200}
,{"name":"com.github.beothorn.App.printNumber(int arg0 = 7)","entryTime":1698585698472,"exitTime":1698585698672,"value":200}
,{"name":"com.github.beothorn.App.printNumber(int arg0 = 8)","entryTime":1698585698672,"exitTime":1698585698873,"value":201}
]}
}
],
[
{"thread":"Thread-2","snapshotTime":1698585700061,"span":{"name":"Thread-2Root","entryTime":1698585697061,"exitTime":-1,"value":0,"children":[{"name":"com.github.beothorn.App.printNumber(int arg0 = 9)","entryTime":1698585698873,"exitTime":1698585699073,"value":200}
,{"name":"com.github.beothorn.App.printNumber(int arg0 = 10)","entryTime":1698585699074,"exitTime":1698585699274,"value":200}
,{"name":"com.github.beothorn.App.printNumber(int arg0 = 11)","entryTime":1698585699274,"exitTime":1698585699475,"value":201}
,{"name":"com.github.beothorn.App.printNumber(int arg0 = 12)","entryTime":1698585699475,"exitTime":1698585699675,"value":200}
,{"name":"com.github.beothorn.App.printNumber(int arg0 = 13)","entryTime":1698585699675,"exitTime":1698585699876,"value":201}
]}
}
],
];