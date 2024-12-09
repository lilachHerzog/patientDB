import React from 'react';

const DocFiles = ({ files }) => {
    return (
        <div>
            <h3>Doc Files</h3>
            <ul>
                {files.map(file => (
                    <li key={file.filename}>{file.filename}</li>
                ))}
            </ul>
        </div>
    );
};

export default DocFiles;
