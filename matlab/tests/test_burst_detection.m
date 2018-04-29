function test_suite = test_burst_detection
    initTestSuite;


function test_burst_detection_

    onsets=[1, 1.1, 1.13, 1.25, 1.42, 1.44, 4];

    bursts=buda_detect_bursts_canonical(onsets);

    expected_bursts=struct();
    expected_bursts.BuNr=[1 2]';
    expected_bursts.nSp=[3 2]';
    expected_bursts.firstSp=[1.1 1.42]';
    expected_bursts.lastSp=[1.25 1.44]';
    expected_bursts.center=[1.175 1.43]';
    expected_bursts.BuDur=[0.15 .02]';
    expected_bursts.SpFreq=[20 100]';
    expected_bursts.interSp=[0.075 .02]';

    assertEqual(fieldnames(expected_bursts), fieldnames(bursts));

    fns=fieldnames(bursts);
    for k=1:numel(fns)
        fn=fns{k};
        assertVectorsAlmostEqual(bursts.(fn),expected_bursts.(fn));
    end


function test_spike_summary
    n_spikes = ceil(rand()*20+30);
    isis = .01*rand(n_spikes-1,1);
    isis(10:end) = isis(10:end)+1;
    isis(20:end) = isis(20:end)+1;

    onsets = cumsum(isis);
    duration = max(onsets) - min(onsets);

    s = buda_summarize_bursts_canonical(onsets);
    assertElementsAlmostEqual(s.avgSpFreq, (n_spikes-1) / duration);



function test_spike_summary_exceptions
    aet=@(varargin) assertExceptionThrown(...
                @()buda_summarize_bursts_canonical(varargin{:},''));

    aet(rand(4));
    aet(struct);
    aet([2 1]);





