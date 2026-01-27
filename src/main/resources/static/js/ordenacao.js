document.addEventListener('DOMContentLoaded', function () {
    const th = document.getElementById('th-dataCriacao');
    if (!th) return;

    const table = th.closest('table');
    if (!table) return;

    const tbody = table.querySelector('tbody');
    if (!tbody) return;

    // toggle state: 'asc' or 'desc'
    th.dataset.order = 'asc';
    const indicator = document.getElementById('criacao-order-indicator');

    function parseDateBR(text) {
        if (!text) return 0;
        // expected dd/MM/yyyy or dd/MM/yyyy HH:mm...
        const parts = text.trim().split(' ')[0].split('/');
        if (parts.length !== 3) return 0;
        const day = parseInt(parts[0], 10);
        const month = parseInt(parts[1], 10) - 1;
        const year = parseInt(parts[2], 10);
        const d = new Date(year, month, day);
        return isNaN(d.getTime()) ? 0 : d.getTime();
    }

    function getCellValue(row, index) {
        const cell = row.children[index];
        return cell ? cell.innerText.trim() : '';
    }

    function updateIndicator(order) {
        if (!indicator) return;
        indicator.textContent = order === 'asc' ? '▲' : '▼';
    }

    th.addEventListener('click', function () {
        const ths = Array.from(th.closest('tr').children);
        const colIndex = ths.indexOf(th);

        const rows = Array.from(tbody.querySelectorAll('tr'))
            // keep only data rows (rows that have td)
            .filter(r => r.querySelectorAll('td').length > 0);

        const order = th.dataset.order === 'asc' ? 'desc' : 'asc';
        th.dataset.order = order;

        rows.sort(function (a, b) {
            const av = parseDateBR(getCellValue(a, colIndex));
            const bv = parseDateBR(getCellValue(b, colIndex));
            if (av === bv) return 0;
            return order === 'asc' ? av - bv : bv - av;
        });

        // re-append in sorted order
        rows.forEach(r => tbody.appendChild(r));

        // keep any "no results" row (with colspan) at the end intact
        const extraRows = Array.from(tbody.querySelectorAll('tr'))
            .filter(r => r.querySelectorAll('td').length === 0 || r.querySelectorAll('td').length !== ths.length);
        extraRows.forEach(r => tbody.appendChild(r));

        updateIndicator(order);
    });

    const btn = document.getElementById('dataCriacao');
    if (btn) {
        btn.addEventListener('click', () => th.click());
    }
});