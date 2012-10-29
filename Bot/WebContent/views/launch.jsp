<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">

	<f:subview id="launchSubview">
		<h:form>
			<h:panelGrid columns="2">
				<h:outputText value="Pince :" />
				<h:column>
					<a4j:commandButton image="../images/arrows/arrow_up_green.png"
						value="Up" action="#{LaunchView.up}" />
					<a4j:commandButton image="../images/arrows/arrow_down_green.png"
						value="Down" action="#{LaunchView.down}" />
					<p /><a4j:commandButton image="../images/motors/gear_run.png"
						value="Open" action="#{LaunchView.open}" /> <a4j:commandButton
						image="../images/motors/gear_delete.png" value="Close"
						action="#{LaunchView.close}" />
				</h:column>
				<h:outputText value="Chargeur :" />
				<h:column>
					<a4j:commandButton image="../images/lock/lock.png" value="Lock"
						action="#{LaunchView.lock}" />
					<a4j:commandButton image="../images/lock/lock_open.png"
						value="Unlock" action="#{LaunchView.unlock}" />
				</h:column>
				<h:outputText value="Lanceur :" />
				<h:column>
					<a4j:commandButton image="../images/arrows/nav_up_right_yellow.png"
						value="Launch" action="#{LaunchView.launch}" />
				</h:column>

				<h:outputText value="Tests :" />
				<h:column>
					<a4j:commandButton value="Sequence 1 (ramassage + lancement)"
						action="#{LaunchView.sequence1}" />
					<a4j:commandButton value="Sequence 2 (lancement)"
						action="#{LaunchView.sequence2}" />
				</h:column>
			</h:panelGrid>
		</h:form>
	</f:subview>

</ui:composition>
