telnet-groovysh
===============

It's a telnet server uses to access spring context beans via groovy-shell


Usage:

Add GroovyShellService bean to spring context.
For security reasons service bound to loopback interface only.

<code>
&lt;bean class="ru.ind.tgs.GroovyShellService" p:listenPort="3333"/&gt;
</code

Run your spring project.

Use telnet to access embedded groovy shell:

<pre>
$ telnet 127.0.0.1 3333
Trying 127.0.0.1...
Connected to 127.0.0.1.
Escape character is '^]'.
Groovy Shell (1.8.5, JVM: 1.7.0_51)
Type 'help' or '\h' for help.
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
groovy:000>
</pre>

Access to spring context via variable "context".

<pre>
groovy:000> context.isActive()
===> true
</pre>

View spring bean names:

<pre>
groovy:000> Arrays.toString(context.getBeanDefinitionNames())
===> [org.springframework.context.annotation.internalConfigurationAnnotationProcessor, ...
...
</pre>

Direct access to your spring beans: your_bean_name.method() or your_bean_name.field
