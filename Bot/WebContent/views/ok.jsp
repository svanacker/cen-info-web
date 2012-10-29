<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core">
<body>

This text above will not be displayed.

<ui:composition template="template.jsp">

This text will not be displayed.

  <ui:define name="title">
    Test
  </ui:define>

This text will also not be displayed.

  <ui:define name="body">
  	Ok
  </ui:define>

This text will not be displayed.
  
</ui:composition>

This text below will also not be displayed.

</body>
</html>
