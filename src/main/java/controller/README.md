<html>
<head>
  
</head>
<body>

<h1>Controller</h1>
<div align="justify">
JTAP follows the  <a href="https://www.baeldung.com/solid-principles"> SOLID design principals</a>

<ul>
  <li>Single Responsibility</li>
  <li>Open/Closed</li>
  <li>Liskov Substitution</li>
  <li>Interface Segregation</li>
  <li>Dependency Inversion</li>
</ul>
  
The dependency inversion is obtined in JTAP using <a href="https://github.com/google/guice">Guice</a>. The controller manage the binds and the Injector.
The default binds between interfaces and implementations are defined in the controllor

```
private static List<AbstractModule> modules = Collections.singletonList(new DefaultModule());
```



</div>

  
  
</body>
</html>


