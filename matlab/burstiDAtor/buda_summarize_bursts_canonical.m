function summary=buda_summarize_bursts_canonical(fn)
% summarizes burst information
%
% summary=buda_summarize_bursts_canonical(fn)
%
% Input:
%  fn           input filename or vector with onsets
%
% Output:
%  summary      struct with a variety of fields (too many to mention)
%
% Example:
%  >> fn='bursts.pr.txt'
%  >> summary=detect_bursts_canonical(fn);
%
% NNO Oct 2013

    if isnumeric(fn)
        spikes=fn;
        short_fn='<numeric>';
        ext='';
    elseif ischar(fn)
        spikes=buda_read_wavemark_onsets(fn);
        [foo,short_fn,ext]=fileparts(fn);
    else
        error('Input must be char of numeric');
    end

    check_spikes(spikes);

    bursts=buda_detect_bursts_canonical(spikes);

    roundup_duration=10; % round up to nearest 10 seconds

    % space for output
    summary=struct();

    %% store general log info
    summary.date=datestr(now);
    summary.analysis='Burstidator v0.1 matlab by Nikolaas N. Oosterhof Jan 2012';
    summary.filename=[short_fn ext];
    summary.path=fn;

    %% spikes
    summary.firstSp=min(spikes);
    summary.lastSp=max(spikes);
    summary.recDur=summary.lastSp-summary.firstSp;
    summary.recDurRndUp=ceil(summary.recDur/roundup_duration)*roundup_duration;
    summary.nSp=numel(spikes);
    summary.avgSpRate=summary.recDur/summary.nSp;
    summary.avgSpRateRndUp=summary.recDurRndUp/summary.nSp;
    summary.avgSpFreq=summary.nSp/summary.recDur;
    summary.avgSpFreqRndUp=summary.nSp/summary.recDurRndUp;

    %% April 2018: added new feature CVI (coefficient of variation)
    isi = diff(spikes);
    summary.CVI = 100 * std(isi) / mean(isi);

    %% Number of bursts
    summary.nBu=numel(bursts.nSp);
    summary.pctSpInBu=100*sum(bursts.nSp)/summary.nSp;

    % compute inter burst intervals
    ibis=bursts.firstSp(2:end)-bursts.lastSp(1:(end-1));

    summary.interBuIvl=sum(ibis)/(summary.nBu-1);
    summary.firstToLastBuCentered=bursts.center(end)-bursts.center(1);
    summary.CycleBu=summary.firstToLastBuCentered/(summary.nBu-1);
    summary.avgBuFreq=summary.nBu/summary.recDur;
    summary.avgBuFreqRndUp=summary.nBu/summary.recDurRndUp;

    %% statistics over bursts

    % fields in bursts to use
    burst_fieldnames={'nSp','BuDur','SpFreq','interSp'};

    % statistics to apply (i.e. a mapping from name to matlab function)
    burst_stats=struct();
    burst_stats.mn=@mean;
    burst_stats.md=@median;
    burst_stats.std=@std;
    stats_fieldnames=fieldnames(burst_stats);

    % use each combination of fields in bursts and stats
    for k=1:numel(burst_fieldnames)
        burst_fieldname=burst_fieldnames{k};
        burst_value=bursts.(burst_fieldname); % get value from struct

        for j=1:numel(stats_fieldnames)
            stat_fieldname=stats_fieldnames{j};

            % get the stat function
            stat_fun=burst_stats.(stat_fieldname);

            % set output fieldname
            full_fieldname=sprintf('%s_%s', stat_fieldname, burst_fieldname);
            summary.(full_fieldname)=stat_fun(burst_value);
        end
    end

function check_spikes(spikes)
    is_not_vector = sum(size(spikes)>1)>1;
    if is_not_vector
        error('Input must be a vector');
    end

    if ~isnumeric(spikes)
        error('Input must be numeric');
    end

    d = diff(spikes);

    if ~all(d>0)
        error('Spike onsets must be increasing');
    end

