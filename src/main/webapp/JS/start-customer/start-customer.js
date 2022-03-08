window.onload = function() {
    selectAllItems();
}

function selectAllItems() {
	var xhttp = new XMLHttpRequest();
	xhttp.open("GET", "rest/customers");
	xhttp.onload = function() {
		var customers = JSON.parse(xhttp.responseText);
		var rows = "";
		for (i = 0; i < customers.length; i++) {
			rows +=
				"<tr>" +
				"<td>" + customers[i].id + "</td>" +
				"<td>" + customers[i].name + "</td>" +
                "<td>" + customers[i].telephone + "</td>" +
                "<td>" + customers[i].address + "</td>" +
				"</tr>";
		}
		document.getElementById("customerTable").innerHTML = rows;
	}
	xhttp.send();
}

function changeCustomer() {
    var itemToUpdate = {
		value: document.getElementById('updateValue').value
	};
    var itemToUpdateJson = JSON.stringify(itemToUpdate);
    var id = document.getElementById('updateId').value;
    var xhttp = new XMLHttpRequest();
    xhttp.open('PUT', 'http://localhost:8080/rest/customers/' + id);
    xhttp.setRequestHeader('Content-Type', 'application/json');
    xhttp.onload = function() {
        selectAllItems();
    }
    xhttp.send(itemToUpdateJson);
}

function deleteItem() {
    var id = document.getElementById('deleteId').value;
    var xhttp = new XMLHttpRequest();
    xhttp.open('DELETE', 'http://localhost:8080/rest/customers/' + id);
    xhttp.onload = function() {
        selectAllItems();
    }
    xhttp.send();
}