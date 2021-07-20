"use strict";

var filterDefaults = {
    UNKNOWN: 'UNKNOWN',
    CONFIRMED: 'CONFIRMED',
    GOOD: 'GOOD',
    BAD: 'BAD',
    NOT_FOUND: 'NOT_FOUND',
    ERROR: 'ERROR'
};

$(function () {
    let $table = $('#table')

    function operateFormatter(value, row, index) {
        return [
            `<a class="check" href="javascript:void(0)" title="check" style="display: none;">`,
            '<i class="fa fa-check"></i>',
            '</a>  ',
            `<a class="edit" href="javascript:void(0)" title="Edit">`,
            '<i class="fa fa-edit"></i>',
            '</a>  ',
            '<a class="remove" href="javascript:void(0)" title="Remove">',
            '<i class="fa fa-trash"></i>',
            '</a>',
            `<a class="save" href="javascript:void(0)" title="save">`,
            '<i class="fa fa-check"></i>',
            '</a>  ',
        ].join('')
    }

    window.operateEvents = {
        'click .edit': function (e, value, row, index) {
            let tableRow = $table.find(`tr[data-index=${index}]`).first();
            tableRow.find("a.edit").css("display", "none");
            tableRow.find("a.link").css("display", "none");

            tableRow.find("input.link").val(tableRow.find("a.link").text());

            tableRow.find("input.link").css("display", "block");
            tableRow.find("a.check").css("display", "");
        },
        'click .check': function (e, value, row, index) {
            let tableRow = $table.find(`tr[data-index=${index}]`).first();
            tableRow.find("input.link").css("display", "none");
            tableRow.find("a.check").css("display", "none");

            $.get(`/updateUrl?feedId=${row.feedId}&url=${tableRow.find("input.link").val()}`)
                .fail(() => alert("Не удалось сохранить ссылку"));

            tableRow.find("a.link").text(tableRow.find("input.link").val());
            tableRow.find("a.link").attr("href", tableRow.find("input.link").val());

            tableRow.find("a.edit").css("display", "");
            tableRow.find("a.link").css("display", "block");
        },
        'click .remove': function (e, value, row, index) {
            alert("Функционал находится в разработке");
            // $table.bootstrapTable('remove', {
            //     field: 'id',
            //     values: [row.id]
            // })
        },
        'click .save': function (e, value, row, index) {
            let tableRow = $table.find(`tr[data-index=${index}]`).first();

            $.get(`/updateUrl?feedId=${row.feedId}&url=${tableRow.find("a.link").attr('href')}`)
                .fail(() => alert("Не удалось сохранить ссылку"));

            tableRow.find(".status").text('CONFIRMED');
        }
    }

    function initTable() {
        $table.bootstrapTable('destroy').bootstrapTable({
            locale: "ru-RU",
            columns: [{
                field: 'feedId',
                align: 'center',
                title: 'ID'
            }, {
                field: 'sku',
                align: 'center',
                title: 'SKU'
            }, {
                field: 'name',
                align: 'center',
                title: 'Название',
            }, {
                field: 'categoryName',
                align: 'center',
                title: 'Категория',
            }, {
                field: 'parseStatus',
                align: 'center',
                title: 'Статус',
                formatter: statusFormatter
            }, {
                field: 'autoSearchUrl',
                align: 'center',
                title: 'Ссылка',
                formatter: linkFormatter
            }, {
                field: 'operate',
                title: 'Операции',
                align: 'center',
                clickToSelect: false,
                events: window.operateEvents,
                formatter: operateFormatter
            }]
        })

        function linkFormatter(value, row, index) {
            return [
                `<input class="link" type="text" style="display: none;width: 100%;">`,
                `<a class="link" target='_blank' href="${row.autoSearchUrl}">${row.autoSearchUrl}</a>`,
            ].join('')
            return $("<a target='_blank'></a>").text(row.autoSearchUrl).attr("href", row.autoSearchUrl).prop('outerHTML');
        }

        function statusFormatter(value, row, index) {
            switch(row.parseStatus) {
                case "UNKNOWN": return "UNKNOWN";
                case "CONFIRMED": return "CONFIRMED";
                case "GOOD": return "GOOD";
                case "BAD": return "BAD";
                case "NOT_FOUND": return "NOT_FOUND";
                default: return "ERROR";
            }
        }
    }

    initTable();
});