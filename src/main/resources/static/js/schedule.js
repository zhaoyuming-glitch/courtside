$(document).ready(function () {

    // 当前日期（格式：2024-01-15）
    var currentDate = new Date();

    // 初始化日期输入框
    function formatDate(d) {
        var y = d.getFullYear();
        var m = String(d.getMonth() + 1).padStart(2, '0');
        var day = String(d.getDate()).padStart(2, '0');
        return y + '-' + m + '-' + day;
    }

    function updateDisplay() {
        var str = formatDate(currentDate);
        $('#dateDisplay').text(str);
        $('#datePicker').val(str);
    }

    function loadGames() {
        var dateStr = formatDate(currentDate);
        $('#gameList').html(
            '<div class="loading">' +
            '<div class="ball">🏀</div>' +
            '<div style="margin-top:12px;">加载中...</div>' +
            '</div>'
        );

        $.get('/api/games/date', { date: dateStr }, function (games) {
            renderGames(games);
        }).fail(function () {
            $('#gameList').html('<div class="empty">❌ 加载失败</div>');
        });
    }

    function renderGames(games) {
        if (!games || games.length === 0) {
            $('#gameList').html('<div class="empty">当天无比赛</div>');
            return;
        }

        var html = '';
        for (var i = 0; i < games.length; i++) {
            var g = games[i];
            var state = g.status_state || 'final';
            var statusText = '';
            var statusCls = '';

            if (state === 'in_progress') {
                statusText = 'LIVE Q' + (g.period || '');
                statusCls = 'live';
            } else if (state === 'scheduled') {
                statusText = '未开始';
                statusCls = 'scheduled';
            } else {
                statusText = '已结束';
                statusCls = 'final';
            }

            var homeScore = g.home_team_score != null ? g.home_team_score : '-';
            var visitorScore = g.visitor_team_score != null ? g.visitor_team_score : '-';
            var homeWin = g.home_team_score > g.visitor_team_score;
            var visitorWin = g.visitor_team_score > g.home_team_score;

            html += '<div class="game-card" data-game-id="' + g.id + '">' +
                '<div class="team-side">' +
                '<div class="team-name">' + (g.home_team.full_name || g.home_team.name) + '</div>' +
                '<div class="team-score ' + (homeWin ? 'win' : '') + '">' + homeScore + '</div>' +
                '</div>' +
                '<div class="vs-mid">VS</div>' +
                '<div class="team-side right">' +
                '<div class="team-name">' + (g.visitor_team.full_name || g.visitor_team.name) + '</div>' +
                '<div class="team-score ' + (visitorWin ? 'win' : '') + '">' + visitorScore + '</div>' +
                '</div>' +
                '<div class="game-status ' + statusCls + '">' + statusText + '</div>' +
                '</div>';
        }
        $('#gameList').html(html);
    }

    // ===== 事件绑定 =====

    $('#prevDay').on('click', function () {
        currentDate.setDate(currentDate.getDate() - 1);
        updateDisplay();
        loadGames();
    });

    $('#nextDay').on('click', function () {
        currentDate.setDate(currentDate.getDate() + 1);
        updateDisplay();
        loadGames();
    });

    $('#datePicker').on('change', function () {
        var val = $(this).val();
        if (val) {
            currentDate = new Date(val + 'T00:00:00');
            updateDisplay();
            loadGames();
        }
    });

    // 点击比赛卡片 → 跳详情
    $('#gameList').on('click', '.game-card', function () {
        var id = $(this).data('game-id');
        if (id) {
            location.href = '/gamedetails?game_id=' + id;
        }
    });

    // ===== 初始化 =====
    updateDisplay();
    loadGames();
});