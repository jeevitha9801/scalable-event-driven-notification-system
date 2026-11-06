let currentPage = 0;
let totalPages = 0;
let distributionChart;

document
    .getElementById("notificationForm")
    .addEventListener("submit", async (event) => {

        event.preventDefault();

        const payload = {
            userId: document.getElementById("userId").value,
            recipient: document.getElementById("recipient").value,
            channel: document.getElementById("channel").value,
            template: document.getElementById("template").value
        };

        try {

            const response = await fetch(
                "http://localhost:8080/notifications",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(payload)
                }
            );

            if (response.ok) {

                document
                    .getElementById("notificationModal")
                    .style.display = "none";

                document
                    .getElementById("notificationForm")
                    .reset();

                await loadNotifications();

                showToast("✅ Notification Sent Successfully");

            } else {

                showToast("❌ Failed to send notification", true);
            }

        } catch (error) {

            console.error(error);

            document
                .getElementById("notificationModal")
                .style.display = "none";

            showToast("⚠️ Error while sending notification", true);
        }
    });

function showToast(message, isError = false) {

    const overlay =
        document.getElementById("toastOverlay");

    const toast =
        document.getElementById("toast");

    toast.textContent = message;

    toast.className = isError
        ? "toast-popup error"
        : "toast-popup";

    overlay.classList.add("show");

    setTimeout(() => {
        overlay.classList.remove("show");
    }, 2500);
}


async function loadNotifications() {

    try {

        const response =
            await fetch(
                `http://localhost:8080/notifications?page=${currentPage}&size=5`
            );

        const result =
            await response.json();

        const data =
            result.content;

        totalPages =
            result.totalPages;

        const tableBody =
            document.querySelector("#notificationTable tbody");

        tableBody.innerHTML = "";

        data.forEach(notification => {

            const row =
                document.createElement("tr");

            row.innerHTML = `
                <td>${notification.id}</td>
                <td>${notification.userId}</td>
                <td>${notification.channel}</td>
                <td class="${
                notification.status === "SENT"
                    ? "sent-status"
                    : "failed-status"
            }">
                    ${notification.status}
                </td>
                <td>${notification.retryCount ?? 0}</td>
                <td>${notification.recipient}</td>
            `;

            tableBody.appendChild(row);
        });

        document.getElementById("pageInfo")
            .innerText =
            `Page ${currentPage + 1} of ${totalPages}`;

        document.getElementById("prevBtn")
            .disabled =
            currentPage === 0;

        document.getElementById("nextBtn")
            .disabled =
            currentPage >= totalPages - 1;

        console.log("loadNotifications");

    } catch (error) {

        console.error(error);

        document.getElementById("total").innerText = "-";
        document.getElementById("sent").innerText = "-";
        document.getElementById("failed").innerText = "-";

        const tableBody =
            document.querySelector("#notificationTable tbody");

        tableBody.innerHTML = `
            <tr>
                <td colspan="6" class="error-message">
                    ⚠️ Database Unavailable
                </td>
            </tr>
        `;
    }
}

async function loadHealthStatus() {

    try {

        const response =
            await fetch(
                "http://localhost:8080/actuator/health"
            );

        const health =
            await response.json();

        console.log(health);
        console.log(
            "RabbitMQ:",
            health.components.rabbit.status
        );

        console.log(
            "DB Status:",
            health.components.db.status
        );

        document
            .getElementById("appStatus")
            .innerHTML =
            health.status === "UP"
                ? "🟢 Application"
                : "🔴 Application";

        document
            .getElementById("dbStatus")
            .innerHTML =
            health.components.db.status === "UP"
                ? "🟢 PostgreSQL"
                : "🔴 PostgreSQL";

        document
            .getElementById("rabbitStatus")
            .innerHTML =
            health.components.rabbit.status === "UP"
                ? "🟢 RabbitMQ"
                : "🔴 RabbitMQ";

    } catch (error) {

        console.error(error);

        document
            .getElementById("appStatus")
            .innerHTML =
            "🔴 Application";

        document
            .getElementById("dbStatus")
            .innerHTML =
            "🔴 PostgreSQL";

        document
            .getElementById("rabbitStatus")
            .innerHTML =
            "🔴 RabbitMQ";
    }
}

document
    .getElementById("nextBtn")
    .addEventListener("click", async () => {

        if (currentPage < totalPages - 1) {

            currentPage++;

            await loadNotifications();
        }
    });

document
    .getElementById("prevBtn")
    .addEventListener("click", async () => {

        if (currentPage > 0) {

            currentPage--;

            await loadNotifications();
        }
    });

async function loadStats() {

    try {

        const response = await fetch(
            "http://localhost:8080/notifications/stats"
        );

        const stats =
            await response.json();

        document.getElementById("total").innerText =
            stats.total;

        document.getElementById("sent").innerText =
            stats.sent;

        document.getElementById("failed").innerText =
            stats.failed;

    } catch (error) {

        console.error(error);

        document.getElementById("total").innerText = "-";
        document.getElementById("sent").innerText = "-";
        document.getElementById("failed").innerText = "-";
    }
}


const modal =
    document.getElementById("notificationModal");

document
    .getElementById("openModalBtn")
    .addEventListener("click", () => {

        modal.style.display = "flex";
    });

document
    .getElementById("closeModal")
    .addEventListener("click", () => {

        modal.style.display = "none";
    });

window.addEventListener("click", (event) => {

    if (event.target === modal) {

        modal.style.display = "none";
    }
});

const historyView =
    document.getElementById("historyView");

const distributionView =
    document.getElementById("distributionView");

document
    .getElementById("historyBtn")
    .addEventListener("click", () => {

        if (historyView.style.display === "block") {

            historyView.style.display = "none";

        } else {

            historyView.style.display = "block";

            distributionView.style.display = "none";
        }
    });

document
    .getElementById("distributionBtn")
    .addEventListener("click", () => {

        if (distributionView.style.display === "block") {

            distributionView.style.display = "none";

        } else {

            distributionView.style.display = "block";

            historyView.style.display = "none";

            renderChart();
        }
    });
async function renderChart() {

    const response =
        await fetch(
            "http://localhost:8080/notifications/stats"
        );

    const stats =
        await response.json();

    document.getElementById("emailCount")
        .innerText = stats.email;

    document.getElementById("smsCount")
        .innerText = stats.sms;

    document.getElementById("pushCount")
        .innerText = stats.push;

    if (distributionChart) {

        distributionChart.destroy();
    }

    const ctx =
        document
            .getElementById("distributionChart")
            .getContext("2d");

    distributionChart =
        new Chart(ctx, {

            type: "doughnut",

            data: {

                labels: [
                    "EMAIL",
                    "SMS",
                    "PUSH"
                ],

                datasets: [{
                    data: [
                        stats.email,
                        stats.sms,
                        stats.push
                    ],

                    backgroundColor: [
                        "#8B5CF6",  // Purple
                        "#22C55E",  // Green
                        "#F59E0B"   // Orange
                    ],

                    borderWidth: 0
                }]
            },

            options: {

                responsive: true,

                maintainAspectRatio: false,

                cutout: "75%",

                interaction: {

                    mode: 'nearest',

                    intersect: true
                },

                animation: {

                    duration: 150
                },

                plugins: {

                    legend: {
                        display: false
                    },

                    tooltip: {

                        enabled: true,

                        backgroundColor: "#1B2740",

                        titleColor: "#FFFFFF",

                        bodyColor: "#FFFFFF",

                        borderColor: "#A855F7",

                        borderWidth: 1,

                        padding: 12,

                        displayColors: true,

                        callbacks: {

                            label: function(context) {

                                return `${context.label}: ${context.raw}`;
                            }
                        }
                    }
                }
            }

        });
}

loadNotifications();
loadHealthStatus();

setInterval(loadNotifications, 5000);
setInterval(loadHealthStatus, 5000);

loadStats();
setInterval(loadStats, 5000);