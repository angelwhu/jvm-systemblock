package com.angelwhu;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ProcessControlException;
import com.alibaba.jvm.sandbox.api.event.BeforeEvent;
import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.filter.NameRegexFilter;
import com.alibaba.jvm.sandbox.api.http.Http;
import com.alibaba.jvm.sandbox.api.listener.EventListener;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Author: angelwhu
 * Date: 2019/01/11
 * Description: 基于JVM-SandBox,阻止恶意命令执行，恶意关机~
 */
@MetaInfServices(Module.class)
@Information(id = "system-block", version = "0.0.1", author = "huanqiwhu@163.com")
public class SystemBlockModule implements Module {
    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    @Http("/blockSystemExec")
    public void blockSystemExec() {
        new EventWatchBuilder(moduleEventWatcher)
                .onClass("java.lang.ProcessBuilder")
                /**/.includeBootstrap()
                .onBehavior("start")
                .onWatch(new AdviceListener() {
                             @Override
                             public void before(Advice advice) throws Throwable {
                                 String cmd = "echo 'your are a good man'";
                                 Runtime run = Runtime.getRuntime();//返回与当前 Java 应用程序相关的运行时对象
                                 Process p = null;
                                 try {
                                     p = run.exec(cmd);
                                 } catch (IOException e) {
                                     e.printStackTrace();
                                 }
                                 // 然后立即返回，因为监听的是BEFORE事件，所以此时立即返回，方法体将不会被执行
                                 ProcessControlException.throwReturnImmediately(p);
                             }
                         }
                );
    }

    @Http("/blockSystemExit")
    public void blockSystemExit() {
        /**
         * 看Java源码，关闭JVM流程会到这3个函数。
         */
        new EventWatchBuilder(moduleEventWatcher)
                .onClass("java.lang.Runtime")
                /**/.includeBootstrap()
                .onBehavior("exit")
                .onWatch(new AdviceListener() {
                             @Override
                             public void before(Advice advice) throws Throwable {
                                 ProcessControlException.throwReturnImmediately(null);
                             }
                         }
                );
        new EventWatchBuilder(moduleEventWatcher)
                .onClass("java.lang.Runtime")
                /**/.includeBootstrap()
                .onBehavior("halt")
                .onWatch(new AdviceListener() {
                             @Override
                             public void before(Advice advice) throws Throwable {
                                 ProcessControlException.throwReturnImmediately(null);
                             }
                         }
                );
        new EventWatchBuilder(moduleEventWatcher)
                .onClass("java.lang.Shutdown")
                /**/.includeBootstrap()
                .onBehavior("halt")
                .onWatch(new AdviceListener() {
                             @Override
                             public void before(Advice advice) throws Throwable {
                                 ProcessControlException.throwReturnImmediately(null);
                             }
                         }
                );
    }

}
