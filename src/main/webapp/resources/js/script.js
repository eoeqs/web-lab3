let x, y, r;
let svg = document.getElementById("svg");
window.redrawDots = redrawPoints
function drawPoint(x, y, r, result) {
    let circle = document.createElementNS("http://www.w3.org/2000/svg", "circle");
    circle.setAttribute("cx", x * (100) / r + 150);
    circle.setAttribute("cy", -y * (100) / r + 150);
    circle.setAttribute("r", 3);
    circle.style.fill = result ? "green" : "red";
    svg.appendChild(circle);
}


function transformSvgToPlane(svgX, svgY, r) {
    console.log(svgX, svgY, r)
    let planeX = (svgX - 150) / (100 / r);
    let planeY = (150 - svgY) / (100 / r);
    return {x: planeX, y: planeY};
}


document.addEventListener("DOMContentLoaded", () => {
    const svg = document.getElementById("svg");
    svg.addEventListener("click", (event) => {

        let point = svg.createSVGPoint();
        point.x = event.clientX;
        point.y = event.clientY;
        let r = PF('RDropDown').getSelectedValue();
        console.log(point.x, point.y, r)
        const svgPoint = point.matrixTransform(svg.getScreenCTM().inverse());
        let planeCoords = transformSvgToPlane(svgPoint.x, svgPoint.y, r);
        addPoint(
            [
                {name: "x", value: planeCoords.x.toString()},
                {name: "y", value: planeCoords.y.toString()},
                {name: "r", value: r.toString()}
            ]
        );

    });
});


function redrawPoints() {
    const table = document.getElementById("table_data");
    if (table) {
        for (let item of table.rows) {
            const x = parseFloat(item.children[0].innerText.trim().replace(",", "."));
            const y = parseFloat(item.children[1].innerText.trim().replace(",", "."));
            const r = parseFloat(item.children[2].innerText.trim().replace(",", "."));

            if (isNaN(x) || isNaN(y) || isNaN(r)) continue;
            const result = item.cells[4].innerText.trim() === "Hit";
            drawPoint(x, y, r, result);
        }
    }
}