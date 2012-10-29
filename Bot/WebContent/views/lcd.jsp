<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">

	<rich:panel id="lcdPanel">
		<h:form id="lcd">
			<h:selectBooleanCheckbox value="#{LCDView.backLight}"
				onchange="document.getElementById('lcd').submit();">Backlight Activated
				</h:selectBooleanCheckbox>
		</h:form>
		<h:form>
			<h:inputText value="#{LCDView.text}" size="10" />
			<br />
			<h:commandButton action="#{LCDView.sendText}" value="Send Text" />
			<br />
			<h:commandButton action="#{LCDView.clearScreen}" value="Clear Screen" />
		</h:form>

	</rich:panel>

</ui:composition>
