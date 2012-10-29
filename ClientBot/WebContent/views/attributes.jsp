<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">

	<f:view>

		<h:dataTable value="#{AttributesView.attributes}" var="attribute">
			<h:column>
				<f:facet name="header">
					<h:outputText value="Nom" />
				</f:facet>
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="Valeur" />
				</f:facet>
				<h:outputText value="#{attribute}" />
			</h:column>
		</h:dataTable>

		<h:form>
			<h:commandButton action="refresh" value="Refresh" />
		</h:form>

	</f:view>

</ui:composition>