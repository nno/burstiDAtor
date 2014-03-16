%% Simple example to run Burstidator functionality in matlab
% NNO Oct 2013

% set paths
% Notes: 
%  - on windows platforms use '/' instead of '\',
%  - end each path with a '/'

rootdir='/Users/nick/organized/201_bursts/exampledata/';
srcdir=[rootdir 'VTA 2 day control/'];
trgdir=[rootdir 'matlab_output'];

% set input file
srcfn=[srcdir '02-ASA-VTA 19-10-11 D3 N7 8930.pr.txt'];

% read onsets
onsets=read_wavemark_onsets(srcfn);

% show onsets
fprintf('Onsets:\n')
disp(onsets)

% read and print bursts
fprintf('Bursts\n')
bursts=detect_bursts_canonical(onsets);
disp(bursts)

% read and print summary
summary=summarize_bursts_canonical(srcfn);
fprintf('Summary:\n')
disp(summary)

% todo: write into text file
