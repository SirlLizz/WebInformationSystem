window.onload = function() {
    selectAllItems();
}

function selectAllItems() {
	var xhttp = new XMLHttpRequest();
	xhttp.open("GET", "rest/customers/");
	xhttp.onload = function() {
        var customers = JSON.parse(xhttp.responseText);
        var rows = "<tr>" +
            "<td>" + 'ID' + "</td>" +
            "<td>" + 'Name' + "</td>" +
            "<td>" + 'PhoneNumber' + "</td>" +
            "<td>" + 'Address' + "</td>" +
            "</tr>";
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
    var id = document.getElementById('updateCustomerChoose').value
    var itemToUpdate = {
        name: document.getElementById('updateName').value,
        phoneNumber: document.getElementById('updatePhone').value,
        address: document.getElementById('updateAddress').value
	};
    var itemToUpdateJson = JSON.stringify(itemToUpdate);
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

function getCustomers() {
    var xhttp = new XMLHttpRequest();
    xhttp.open("GET", "rest/customers/");
    xhttp.onload = function() {
        var customers = JSON.parse(xhttp.responseText);
        let rows = '';
        for (let i = 0; i < customers.length; i++) {
            rows +=
                "<option value = " + customers[i].customerID + ">" +
                customers[i].name + "</option>";
        }
        console.log(rows)
        console.log(customers)
        document.getElementById("updateCustomerChoose").innerHTML = rows;
    }
    xhttp.send();
}
