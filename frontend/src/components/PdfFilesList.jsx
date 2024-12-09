import React from 'react';

const PdfFiles = ({ files }) => {
    return (
        <div>
            <h3>Pdf Files</h3>
            <ul>
                {files.map(file => (
                    <li key={file.filename}>{file.filename}</li>
                ))}
            </ul>
        </div>
    );
};

export default PdfFiles;
