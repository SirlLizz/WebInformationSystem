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
                "<td>" + customers[i].customerID + "</td>" +
                "<td>" + customers[i].name + "</td>" +
                "<td>" + customers[i].phoneNumber + "</td>" +
                "<td>" + customers[i].address + "</td>" +
                "</tr>";
        }
        document.getElementById("customerTable").innerHTML = rows;
    }
	xhttp.send();
}

function changeCustomer() {
    var itemToUpdate = {
		value: document.getElementById('updateId').value
	};
    var itemToUpdateJson = JSON.stringify(itemToUpdate);
    var id = document.getElementById('updateId').value;
    var xhttp = new XMLHttpRequest();
    xhttp.open('PUT', 'rest/customers/' + id);
    xhttp.setRequestHeader('Content-Type', 'application/json');
    xhttp.onload = function() {
        selectAllItems();
    }
    xhttp.send(itemToUpdateJson);
}

function deleteItem() {
    var id = document.getElementById('deleteId').value;
    var xhttp = new XMLHttpRequest();
    xhttp.open('DELETE', 'rest/customers/' + id);
    xhttp.onload = function() {
        selectAllItems();
    }
    xhttp.send();
}
