<%@ include file="/WEB-INF/views/common/include.jsp" %>

<style>

td.setting {
width: 40%;
}

</style>
<form:form id="setting" modelAttribute="setting" action="setting/save" method="post">
<fieldset>
    <legend>Search Setting</legend>

<table>

<tr>
    <td class="setting">
    Annotation Integration
    </td>

    <td>
        Enable: <form:radiobutton path="annotate" value="true" />
        Disable: <form:radiobutton path="annotate" value="false"/>
    </td>

</tr>

<tr>
    <td class="setting">
    Google Scholar Integration
    </td>

    <td>
        Enable: <form:radiobutton path="googleScholar" value="true" />
        Disable: <form:radiobutton path="googleScholar" value="false"/>
    </td>

</tr>

<tr>
    <td class="setting">
    Mendeley Integration
    </td>

    <td>
        Enable: <form:radiobutton path="mendeley" value="true" />
        Disable: <form:radiobutton path="mendeley" value="false"/>
    </td>

</tr>

<tr>
<td> </td>
<td>
   <p>
        <input type="submit" value="Save Settings"/>
    </p>
    </td>
</tr>
</table>

</fieldset>
 </form:form>

