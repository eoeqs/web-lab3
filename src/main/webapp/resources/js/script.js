let r;
let svg = document.getElementById("svg");
window.redrawDots = redrawPoints
window.handleRRRChange = handleRRChange
function drawPoint(x, y, r, result) {
    let circle = document.createElementNS("http://www.w3.org/2000/svg", "circle");
    circle.setAttribute("cx", x * (100) / r + 150);
    circle.setAttribute("cy", -y * (100) / r + 150);
    circle.setAttribute("r", 3);
    circle.style.fill = result ? "green" : "red";
    svg.appendChild(circle);

    let rLabels = document.querySelectorAll(".rLabel");

    rLabels.forEach((label) => {
        label.textContent = label.textContent.replace(/(-?)R(\/2)?/g, (_, sign, isHalf) => {
            if (isHalf === "/2") {
                return sign + r + "/2";
            } else {
                return sign + r;
            }
        });
    });
}


function transformSvgToPlane(svgX, svgY, r) {
    let planeX = (svgX - 150) / (100 / r);
    let planeY = (150 - svgY) / (100 / r);
    return {x: planeX, y: planeY};
}


document.addEventListener("DOMContentLoaded", () => {
    const svg = document.getElementById("svg");
    svg.addEventListener("click", async (event) => {

        let point = svg.createSVGPoint();
        point.x = event.clientX;
        point.y = event.clientY;
        let r = PF('RDropDown').getSelectedValue();
        const svgPoint = point.matrixTransform(svg.getScreenCTM().inverse());
        let planeCoords = transformSvgToPlane(svgPoint.x, svgPoint.y, r);
        response = await addPoint(
            [{name: "x", value: planeCoords.x.toString()},
                {name: "y", value: planeCoords.y.toString()},
                {
                    name: "r", value: r.toString()
                }]);
        drawPoint(planeCoords.x, planeCoords.y, parseInt(r), response.jqXHR.pfArgs.result)
    });




});


function redrawPoints() {
    const table = document.getElementById("table_data");

    document.querySelectorAll("circle").forEach((i) => {
        i.remove()
    });
    if (table) {
        for (let item of table.rows) {
            const x = parseFloat(item.children[0].innerText.trim().replace(",", "."));
            const y = parseFloat(item.children[1].innerText.trim().replace(",", "."));
            const r = parseFloat(item.children[2].innerText.trim().replace(",", "."));
            const result = item.children[3].innerText.trim() === "Hit";
            console.log(x,y,r,result)
            if (isNaN(x) || isNaN(y) || isNaN(r)) continue;
            drawPoint(x, y, r, result);
        }
    }

}
function handleRRChange() {
    let r = PF('RDropDown').getSelectedValue();
    let rLabels = document.querySelectorAll(".rLabel");
    rLabels.forEach((label) => {
        label.textContent = label.textContent.replace(/(-?).(\/2)?/g, (_, sign, isHalf) => {
            if (isHalf === "/2") {
                return sign + r + "/2";
            } else {
                return sign + r;
            }
        });
    });

    const table = document.getElementById("table_data");

    document.querySelectorAll("circle").forEach((i) => {
        i.remove()
    });

    if (table) {
        for (let item of table.rows) {
            const x = parseFloat(item.children[0].innerText.trim().replace(",", "."));
            const y = parseFloat(item.children[1].innerText.trim().replace(",", "."));
            const r = parseFloat(PF('RDropDown').getSelectedValue());
            const result = item.children[3].innerText.trim() === "Hit";
            console.log(x,y,r,result)
            if (isNaN(x) || isNaN(y) || isNaN(r)) continue;
            drawPoint(x, y, r, result);
        }
    }
}


