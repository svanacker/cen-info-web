<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">

	<rich:panel id="sonarCountPanel">
		<h:form>
			<h:outputLabel for="selectCount" value="Nombre de sonars :" />
			<h:selectOneMenu id="selectCount" value="#{SonarView.count}">
				<c:forEach var="item" begin="1" end="#{SonarView.maxCount}">
					<f:selectItem itemValue="#{item}" />
				</c:forEach>
				<a4j:support event="onchange" reRender="sonarPanel,sonarCountPanel" />
			</h:selectOneMenu>
		</h:form>
	</rich:panel>
	<rich:panel id="sonarPanel">
		<c:forEach var="item" begin="1" end="#{SonarView.count}">
			<rich:panel>
				<f:facet name="header">
					<h:outputText
						value="Sonar #{item} : distance=#{SonarView.data[item-1].value} cm" />
				</f:facet>
			</rich:panel>
		</c:forEach>
	</rich:panel>

</ui:composition>
