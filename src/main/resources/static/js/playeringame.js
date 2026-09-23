$(document).ready(function() {
    var params = new URLSearchParams(window.location.search);
    var playerId = params.get('player_id');
    var gameId = params.get('game_id');

    if (!playerId || !gameId) {
        $('#loadingWrapper').hide();
        $('#pageContent').show();
        $('.container').html('<div style="text-align:center;padding:80px 20px;font-size:20px;font-weight:900;">❌ 缺少必要参数<br><span style="font-size:14px;color:#999;margin-top:12px;display:block;">请从比赛详情页点击球员进入</span></div>');
        return;
    }

    // ===== 评分相关变量 =====
    var userId = 1;
    var ratingTipTimer = null;

    // ===== 加载页面数据 =====
    $.get('/api/games/' + gameId, function(game) {
        var season = game.season || new Date().getFullYear();
        $.when(
            $.get('/api/player/' + playerId),
            $.get('/api/stats/SeasonAvg?playerId=' + playerId + '&season=' + season),
            $.get('/api/stats/PlayerSingleGameStats?playerId=' + playerId + '&gameId=' + gameId)
        ).done(function(playerResp, seasonResp, statsResp) {
            $('#loadingWrapper').hide();
            $('#pageContent').show();
            var player = playerResp[0];
            if (player) {
                $('.nav-title').text(player.first_name + ' ' + player.last_name);
                $('.player-info h1').text(player.first_name + ' ' + player.last_name);

                var metaHtml = (player.team ? (player.team.full_name || player.team.name || '自由球员') : '自由球员');
                metaHtml += ' <span>·</span> ' + (player.position || 'N/A');
                metaHtml += ' <span>·</span> #' + (player.jersey_number || '--');
                $('.player-meta').html(metaHtml);
            }
            var seasonData = seasonResp[0];
            if (seasonData) {
                var avgHtml = '本赛季场均 ';
                avgHtml += '<strong>' + (seasonData.pointsStr || '--') + '</strong> 分';
                avgHtml += ' · <strong>' + (seasonData.reboundsStr || '--') + '</strong> 板';
                avgHtml += ' · <strong>' + (seasonData.assistsStr || '--') + '</strong> 助';
                $('.player-season-avg').html(avgHtml);
            } else {
                $('.player-season-avg').text('本赛季暂无数据');
            }
            var stat = statsResp[0];
            if (stat) {
                $('.game-stats-grid .stat-item:eq(0) .stat-value').text(stat.pts || 0);
                $('.game-stats-grid .stat-item:eq(1) .stat-value').text(stat.reb || 0);
                $('.game-stats-grid .stat-item:eq(2) .stat-value').text(stat.ast || 0);
                $('.game-stats-grid .stat-item:eq(3) .stat-value').text(stat.stl || 0);
                $('.game-stats-grid .stat-item:eq(4) .stat-value').text(stat.blk || 0);

                var fgm = stat.fgm || 0;
                var fga = stat.fga || 0;
                var fg3m = stat.fg3m || 0;
                var fg3a = stat.fg3a || 0;
                var ftm = stat.ftm || 0;
                var fta = stat.fta || 0;

                $('.game-stats-grid .stat-item:eq(5) .stat-value').text(fgm + '/' + fga);
                $('.game-stats-grid .stat-item:eq(6) .stat-value').text(fg3m + '/' + fg3a);
                $('.game-stats-grid .stat-item:eq(7) .stat-value').text(ftm + '/' + fta);
            } else {
                $('.game-stats-grid .stat-item .stat-value').text('--');
            }

            loadPlayerRating();

        }).fail(function() {
            $('#loadingWrapper').hide();
            $('#pageContent').show();
            $('.container').html('<div style="text-align:center;padding:80px 20px;font-size:20px;font-weight:900;">❌ 加载失败<br><span style="font-size:14px;color:#999;margin-top:12px;display:block;">请检查网络连接或后端服务</span></div>');
        });

    }).fail(function() {
        $('#loadingWrapper').hide();
        $('#pageContent').show();
        $('.container').html('<div style="text-align:center;padding:80px 20px;font-size:20px;font-weight:900;">❌ 获取比赛信息失败<br><span style="font-size:14px;color:#999;margin-top:12px;display:block;">请检查比赛 ID 是否正确</span></div>');
    });

    // ===== 加载评分数据 =====
    function loadPlayerRating() {
        $.get('/courtside/rating/getPlayerRating', {
            player_id: playerId,
            game_id: gameId,
            user_id: userId
        }, function(resp) {
            if (resp.userScore != null) {
                highlightStars(resp.userScore);
            }
            $('#avgRating').text(resp.avgRating != null ? resp.avgRating : '--');
            $('#ratingCount').text(resp.ratingCount != null ? resp.ratingCount : '--');
        }).fail(function() {
            $('#avgRating').text('--');
            $('#ratingCount').text('--');
        });
    }

    // ===== 点星星提交评分 =====
    $('#userStars').on('click', 'span', function() {
        var score = $(this).index() + 1;
        submitRating(score);
    });

    function submitRating(score) {
        $.post('/courtside/rating/submitRating', {
            user_id: userId,
            player_id: playerId,
            game_id: gameId,
            score: score
        }, function(result) {
            var tipMap = {
                'FIRST_SUCCESS': '已评星',
                'UPDATE_SUCCESS': '修改成功',
                'FIRST_FAIL': '评星失败',
                'UPDATE_FAIL': '修改失败'
            };

            if (tipMap[result]) {
                // 正常返回
                var isSuccess = (result === 'FIRST_SUCCESS' || result === 'UPDATE_SUCCESS');
                if (isSuccess) {
                    highlightStars(score);
                    loadPlayerRating();
                }
                showRatingTip(tipMap[result]);
            } else {
                // 被 Spring Security 拦截，返回的是登录页 HTML → 跳登录页
                location.href = '/login.html';
            }
        }).fail(function() {
            // 302 也会被 jQuery 当失败处理
            location.href = '/login.html';
        });
    }

    // ===== 星星高亮 =====
    function highlightStars(score) {
        $('#userStars span').each(function(i) {
            $(this).toggleClass('star-active', i < score);
        });
    }

    // ===== 提示文字显示/隐藏 =====
    function showRatingTip(text) {
        var $tip = $('#ratingTip');
        $tip.text(text).addClass('show');
        if (ratingTipTimer) clearTimeout(ratingTipTimer);
        ratingTipTimer = setTimeout(function() {
            $tip.removeClass('show');
        }, 2000);
    }
});