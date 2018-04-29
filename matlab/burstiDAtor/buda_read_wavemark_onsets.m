function onsets=buda_read_wavemark_onsets(fn)
% reads a wavemark file and returns the onset of each wavemark
%
% onsets=buda_read_wavemark_onsets(fn)
%
% Input:
%   fn        filename of wavemark file
%
% Output:
%   onsets    1xP vector with onsets in fn, if there are P onsets
%
% NNO Oct 2013

    % set prefix indicating start of new wavemark
    prefix='"WAVMK"	';

    % check if file exists; if not throw an error.
    % this gives a more informative error message than what would be given by
    % fopen used below.
    if ~exist(fn,'file')
        error('File not found: %s', fn);
    end

    % open file
    fid=fopen(fn);

    % allocate space for output - with space for 100 onsets to start with
    onsets=zeros(1,100);

    % counter to keep track of number of onsets found so far
    n=0;

    % read each line in input file
    while true
        line=fgetl(fid);
        if ~ischar(line)
            % end of file; done reading file and break out of loop
            break
        end

        % file contains the prefix
        if findstr(prefix,line)
            % find 'tab' character positions
            tabpos=find(line==sprintf('\t'));

            % onset is between first and second tab
            onset=str2num(line(((tabpos(1)+1):(tabpos(2)-1))));

            % increment counter
            n=n+1;

            if n>numel(onsets)
                % more onsets than space - double the size of onsets. note:
                % - this implicitly fills all positions in onsets((n+1):end)
                %   with zeros.
                % - changing the size of onsets requires new memory allocation
                %   which is expensive; by doubling the size every time
                %   there are at most log2(P/100) allocation steps
                %   for P onsets in the input file).
                onsets(2*n)=0;
            end

            % store onset
            onsets(n)=onset;
        end
    end

    % close the file
    fclose(fid);

    % get rid of unused space in onsets
    onsets=onsets(1:n);
