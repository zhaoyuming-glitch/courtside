var game, statsLoaded = false, recapLoaded = false;

$(function() {
    var id = new URLSearchParams(location.search).get('game_id');
    if (!id) return alert('缺少比赛 ID');

    $.get('/api/games/' + id, function(data) {
        game = data;
        $('#loadingWrapper').hide();
        $('#pageContent').show();
        renderGame(data);
        loadPlayers(id);
    }).fail(function() { alert('加载失败'); });

    $('.tab-btn').on('click', function() {
        var tab = $(this).data('tab');
        $('.tab-btn').removeClass('active');
        $(this).addClass('active');
        $('.tab-content').hide();
        $('#tab-' + tab).show();
        if (tab === 'stats' && !statsLoaded) loadStats(id);
        if (tab === 'recap' && !recapLoaded) loadRecap(id);
    });

    $('#homePlayerList, #visitorPlayerList').on('click', '.player-card', function() {
        var playerId = $(this).data('player-id');
        var gameId = $(this).data('game-id');
        if (playerId) {
            location.href = '/playeringame?player_id=' + playerId + '&game_id=' + gameId;
        }
    });

    $('#regenerateRecap').on('click', function() {
        $('#recapBody').html('<div class="recap-loading">' +
            '<div class="ball">🏀</div>' +
            '<div class="text">AI 重新生成中...</div>' +
            '</div>');

        $.get('/courtside/recap/regenerate/' + id, function(content) {
            renderRecap(content);
        }).fail(function(xhr) {
            if (xhr.status === 401) {
                location.href = '/login.html';
            } else if (xhr.status === 403) {
                $('#recapBody').html('<div style="text-align:center;padding:50px 20px;">' +
                    '<div style="font-size:48px;margin-bottom:16px;">🔒</div>' +
                    '<div style="font-size:16px;font-weight:900;color:#999;">VIP 专属功能</div>' +
                    '</div>');
            } else {
                $('#recapBody').html('<div style="text-align:center;padding:40px;color:#999;font-weight:700;">' +
                    '❌ 生成失败，请重试' +
                    '</div>');
            }
        });
    });
});

function renderGame(game) {
    $('.nav-title').text(game.home_team.name + ' VS ' + game.visitor_team.name);
    $('.sb-name:eq(0)').text(game.home_team.name);
    $('.sb-name:eq(1)').text(game.visitor_team.name);
    $('.sb-score:eq(0)').text(game.home_team_score);
    $('.sb-score:eq(1)').text(game.visitor_team_score);

    var state = game.status_state || 'final';
    var tagMap = { in_progress: 'LIVE', scheduled: 'UPCOMING', final: 'FINAL' };
    var clsMap = { in_progress: 'live', scheduled: 'upcoming', final: 'final' };
    var detail = state === 'in_progress' ? ' · ' + game.status : ' · ' + game.date + ' ' + game.status;

    $('.sb-status').removeClass('upcoming final').addClass(clsMap[state] || 'final')
        .html('<span>' + tagMap[state] + detail + '</span>');

    $('.sb-score').removeClass('leading');
    if (game.home_team_score > game.visitor_team_score) $('.sb-score:eq(0)').addClass('leading');
    else if (game.visitor_team_score > game.home_team_score) $('.sb-score:eq(1)').addClass('leading');
}

function loadPlayers(id) {
    $.when(
        $.get('/api/stats/homeTeamStats?gameId=' + id),
        $.get('/api/stats/visitorTeamStats?gameId=' + id)
    ).done(function(home, visitor) {
        $('#homeRosterTitle').text('🏠 ' + game.home_team.name);
        $('#visitorRosterTitle').text('✈️ ' + game.visitor_team.name);
        renderPlayers(home[0], 'homePlayerList');
        renderPlayers(visitor[0], 'visitorPlayerList');
    });
}

function renderPlayers(players, container) {
    if (!players || !players.length) {
        $('#' + container).html('<div style="padding:40px;text-align:center;color:#999;">暂无数据</div>');
        return;
    }
    var html = '';
    for (var i = 0; i < players.length; i++) {
        var p = players[i];
        var pl = p.player;
        html += '<div class="player-card" data-player-id="' + (pl.id || 0) + '" data-game-id="' + game.id + '">' +
            '<div class="player-avatar">🏀</div>' +
            '<div class="player-info"><div class="player-name">' + (pl.first_name || '') + ' ' + (pl.last_name || '') + '</div>' +
            '<div class="player-pos">' + (pl.position || 'N/A') + '</div></div>' +
            '<div class="player-stats">' +
            '<span><strong>' + (p.pts || 0) + '</strong>分</span>' +
            '<span><strong>' + (p.reb || 0) + '</strong>板</span>' +
            '<span><strong>' + (p.ast || 0) + '</strong>助</span>' +
            '<span><strong>' + (p.stl || 0) + '</strong>抢</span>' +
            '<span><strong>' + (p.blk || 0) + '</strong>盖</span>' +
            '</div><div class="player-rate">点击评分</div></div>';
    }
    $('#' + container).html(html);
}

function loadStats(id) {
    $.when(
        $.get('/api/stats/homeTeamTotals?gameId=' + id),
        $.get('/api/stats/visitorTeamTotals?gameId=' + id)
    ).done(function(home, visitor) {
        renderStats(home[0], visitor[0]);
        statsLoaded = true;
    });
}

function renderStats(home, visitor) {
    $('#homeTeamHeader').text('🟡 ' + game.home_team.name);
    $('#visitorTeamHeader').text('🟣 ' + game.visitor_team.name);
    var fields = ['fg_pct', 'fg3_pct', 'ft_pct', 'reb', 'ast', 'stl', 'blk', 'turnover', 'pf'];
    var ids = ['FgPct', 'Fg3Pct', 'FtPct', 'Reb', 'Ast', 'Stl', 'Blk', 'Turnover', 'Pf'];
    var isLower = ['turnover', 'pf'];

    for (var i = 0; i < fields.length; i++) {
        var f = fields[i];
        var hv = home[f] !== undefined ? home[f] : '--';
        var vv = visitor[f] !== undefined ? visitor[f] : '--';

        var hBetter = false, vBetter = false;
        if (typeof hv === 'number' && typeof vv === 'number') {
            var lower = isLower.indexOf(f) > -1;
            hBetter = lower ? hv < vv : hv > vv;
            vBetter = lower ? vv < hv : vv > hv;
        }

        $('#home' + ids[i]).text(hv).removeClass('stat-win').toggleClass('stat-win', hBetter);
        $('#visitor' + ids[i]).text(vv).removeClass('stat-win').toggleClass('stat-win', vBetter);
    }
}

function loadRecap(id) {
    $('#recapBody').html('<div class="recap-loading">' +
        '<div class="ball">🏀</div>' +
        '<div class="text">AI 生成中...</div>' +
        '</div>');

    $.get('/courtside/recap/' + id, function(content) {
        renderRecap(content);
        recapLoaded = true;
    }).fail(function(xhr) {
        if (xhr.status === 401) {
            location.href = '/login.html';
        } else if (xhr.status === 403) {
            $('#recapBody').html('<div style="text-align:center;padding:50px 20px;">' +
                '<div style="font-size:48px;margin-bottom:16px;">🔒</div>' +
                '<div style="font-size:16px;font-weight:900;color:#999;">VIP 专属功能</div>' +
                '<div style="font-size:13px;font-weight:700;color:#bbb;margin-top:8px;">升级 VIP 后即可查看 AI 赛况</div>' +
                '</div>');
        } else {
            $('#recapBody').html('<div style="text-align:center;padding:40px;color:#999;font-weight:700;">' +
                '❌ 生成失败，请点击重新生成' +
                '</div>');
        }
    });
}

function renderRecap(content) {
    if (!content) {
        $('#recapBody').html('<div style="text-align:center;padding:40px;color:#999;font-weight:700;">暂无内容</div>');
        return;
    }
    var paragraphs = content.split('\n\n');
    var html = '';
    for (var i = 0; i < paragraphs.length; i++) {
        var p = paragraphs[i].trim();
        if (p) {
            html += '<p class="recap-para">' + p + '</p>';
        }
    }
    $('#recapBody').html(html);
}